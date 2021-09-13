package com.medusa.gruul.common.rabbitmq.core;

import com.medusa.gruul.common.rabbitmq.interfaces.ReConnectionListener;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

/**
 * @author wangpeng
 * @version V1.0
 * @data 2018-07-08下午1:41:10
 * @description TODO
 */
public class SmsSingleMqHelper implements ReConnectionListener {
    private final static Logger logger = LoggerFactory.getLogger(SmsSingleMqHelper.class);
    private static SmsSingleMqHelper sInstance;
    private BaseMq provider;
    private ExecutorService runJobPool;

    private ConnectionFactory factory = null;

    private String queueName;
    private String exchangeName;
    private String severity;
    private String delayQueue;
    private int connectionNum = 4;//默认是4个链接
    private int channelNum = 4;//默认是每个链接4个channel
    private String exchangeType = "direct";
    private String consumerClazz;

    private List<Channel> channelList = new ArrayList<Channel>();
    private List<Connection> connectionList = new ArrayList<Connection>();

    private boolean initExchange = true;

    private SmsSingleMqHelper(BaseMq provider) {
        this.provider = provider;
        this.factory = provider.getConnectionFactory();
        provider.registerListener(this);
    }

    public synchronized static SmsSingleMqHelper getInstance(BaseMq provider) {
        if (sInstance == null) {
            sInstance = new SmsSingleMqHelper(provider);
        }
        return sInstance;
    }

    @Override
    public void resetConnectionFactory(ConnectionFactory connectionFactory) {
        try {
            shutdown();
            this.factory = connectionFactory;
            initConnection();
            runConsume(null);
        } catch (Exception e) {
            logger.error("重连出错", e);
        }
    }

    public void initConnection(int connectionNum, int channelNum, String queueName, String exchangeName, String severity, String exchangeType, String delayQueue) throws IOException, TimeoutException {
        this.connectionNum = connectionNum;
        this.channelNum = channelNum;
        this.queueName = queueName;
        this.exchangeName = exchangeName;
        this.severity = severity;
        this.exchangeType = exchangeType;
        this.delayQueue = delayQueue;
        initConnection();
    }

    private void initConnection() throws IOException, TimeoutException {
        for (int i = 0; i < this.connectionNum; i++) {
            Connection connection = this.factory.newConnection();
            connectionList.add(connection);
        }
    }

    public void shutdown() {
        logger.info("Message Queue start to shutdown");

        if (channelList != null && channelList.size() > 0) {
            for (Channel channel : channelList) {
                closeChannel(channel);
            }
            channelList.clear();
        }

        if (connectionList != null && connectionList.size() > 0) {
            for (Connection connection : connectionList) {
                closeConnection(connection);
            }
            connectionList.clear();
        }

        PoolUtil.shutdownAndAwaitTermination(this.runJobPool, 90);
        logger.info("Message Queue shutdown completed");
    }


    private void closeConnection(Connection connection) {
        if (connection != null && connection.isOpen()) {
            try {
                connection.close();
            } catch (Exception e) {
                logger.error("关闭connection出错", e);
            }
        }
    }

    private void closeChannel(Channel channel) {
        if (channel != null && channel.isOpen()) {
            try {
                channel.close();
            } catch (Exception e) {
                logger.error("关闭channel出错", e);
            }
        }
    }

    /**
     * 开始消费消息
     *
     * @throws IOException
     */

    public void startConsume(String consumerClazz, Boolean isdDelay) throws IOException {
        this.consumerClazz = consumerClazz;
        runConsume(isdDelay);
    }

    private void runConsume(Boolean isDelay) {
        this.runJobPool = Executors.newFixedThreadPool(this.connectionNum * this.channelNum);
        for (int i = 0; i < this.connectionNum; i++) {
            for (int j = 0; j < this.channelNum; j++) {
                runJobs(runJobPool, i, isDelay);
            }
        }
    }

    private void runJobs(ExecutorService executorService, final int i, Boolean isdDelay) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Channel channel = connectionList.get(i).createChannel();
                    channelList.add(channel);

                    boolean durable = true;    //设置转发器、队列持久化
                    //声明持久化转发器的类型
                    if (isdDelay) {
                        HashMap<String, Object> arguments = new HashMap<String, Object>();
                        arguments.put("x-dead-letter-exchange", "amq.direct");
                        arguments.put("x-dead-letter-routing-key", "message_ttl_routingKey");
                        channel.queueDeclare(delayQueue, true, false, false, arguments);

                        // 声明队列
                        channel.queueDeclare(queueName, true, false, false, null);
                        // 绑定路由
                        channel.queueBind(queueName, "amq.direct", "message_ttl_routingKey");
                    } else {
                        if (initExchange) {
                            channel.exchangeDeclare(exchangeName, exchangeType == null ? "direct" : exchangeType, durable);
                        }
                        //声明持久化队列
                        channel.queueDeclare(queueName, durable, false, false, null);
                        //转发器与队列绑定
                        channel.queueBind(queueName, exchangeName, severity); //routing key
                        channel.basicQos(1, true);
                    }
                    //反射指定运行类
                    Constructor con = Class.forName(consumerClazz).getConstructor(Channel.class);
                    DefaultConsumer consumer = (DefaultConsumer) con.newInstance(channel);
                    //指定消费队列，增加消息应答
                    channel.basicConsume(queueName, false, consumer);
                } catch (Exception e) {
                    logger.error("basicConsume Exception ", e);
                }
            }
        });
    }

    /*private void initConsumerChannel(String queueName) throws IOException {
        HashMap<String, Object> arguments = new HashMap<String, Object>();
        arguments.put("x-dead-letter-exchange", "amq.direct");
        arguments.put("x-dead-letter-routing-key", "message_ttl_routingKey");
        channel.queueDeclare(queueName, true, false, false, arguments);
        // 声明队列
        channel.queueDeclare(queueName, true, false, false, null);
        // 绑定路由
        channel.queueBind(queueName, "amq.direct", "message_ttl_routingKey");
        QueueingConsumer consumer = new QueueingConsumer(channel);
        // 指定消费队列
        channel.basicConsume(queueName, true, consumer);

    }*/
    public void setInitExchange(boolean initExchange) {
        this.initExchange = initExchange;
    }

}
