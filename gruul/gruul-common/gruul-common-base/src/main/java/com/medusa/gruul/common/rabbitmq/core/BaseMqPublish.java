
package com.medusa.gruul.common.rabbitmq.core;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


/**
 * @author wangpeng
 */

public class BaseMqPublish {

    private final static Logger logger = LoggerFactory.getLogger(BaseMqPublish.class);


     /**
     * 发送消息
     */
    public static boolean publishMessage(String queueName, String msg, MqSelector selector) {
        Channel channel = null;
        try {
            channel = BaseMq.getInitedInstance(selector).getChannel();
            channel.confirmSelect();
            channel.waitForConfirms();
            return publishMessage(queueName,msg,channel);
        } catch (Exception e) {
            logger.error(selector + "发送消息异常", e);
            //insertErrorToDb(type, message, exchangeName, severity, e);
        } finally {
            try {
                BaseMq.getInitedInstance(selector).returnChannel(channel);
            } catch (Exception e) {
                logger.error(selector + "发送消息异常", e);
            }
        }
        return false;
    }


    /**
     * 发送消息
     */
    public static boolean publishMessage(byte[] message, MqSelector selector, String exchangeName, String severity,String queueName) {
        Channel channel = null;
        try {
            channel = BaseMq.getInitedInstance(selector).getChannel();
            channel.confirmSelect();
            channel.waitForConfirms();
            return publishMessage(queueName, message, channel, exchangeName, severity);
        } catch (Exception e) {
            logger.error(selector + "发送消息异常", e);
            //insertErrorToDb(type, message, exchangeName, severity, e);
        } finally {
            try {
                BaseMq.getInitedInstance(selector).returnChannel(channel);
            } catch (Exception e) {
                logger.error(selector + "发送消息异常", e);
            }
        }
        return false;
    }

    /**
     * 发送延时消息
     */
    public static boolean publishDlMsg(byte[] message, MqSelector selector,String delayQueue, String ttlQueue,String expires) {
        Channel channel = null;
        String type = selector.getKey();
        try {
            channel = BaseMq.getInitedInstance(selector).getChannel();
            channel.confirmSelect();
            channel.waitForConfirms();
            HashMap<String, Object> arguments = new HashMap<String, Object>();
            arguments.put("x-dead-letter-exchange", "amq.direct");
            arguments.put("x-dead-letter-routing-key", "message_ttl_routingKey");
            channel.queueDeclare(delayQueue, true, false, false, arguments);
            // 声明队列
            channel.queueDeclare(ttlQueue, true, false, false, null);
            // 绑定路由
            channel.queueBind(ttlQueue, "amq.direct", "message_ttl_routingKey");
            return publishDlMsg(type, message, channel,delayQueue,ttlQueue,expires);
        } catch (Exception e) {
            logger.error(selector + "发送消息异常", e);
            //insertErrorToDb(type, message, null, null, e);
        } finally {
            try {
                BaseMq.getInitedInstance(selector).returnChannel(channel);
            } catch (Exception e) {
                logger.error(selector + "发送消息异常", e);
            }
        }
        return false;
    }


    private static boolean publishDlMsg(String type, byte[] message, Channel channel, String delayQueue, String ttlQueue, String expires) {
        boolean ret = Boolean.TRUE;
        String publishInfo = new String(message);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(new Date());
        try {
            // 设置延时属性
            AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
            // 持久性 non-persistent (1) or persistent (2)
            AMQP.BasicProperties properties = builder.expiration(expires).deliveryMode(2).build();
            // routingKey =delay_queue 进行转发
            channel.basicPublish("", delayQueue, properties, message);
            logger.debug("开始发送延时：消息 " + publishInfo + " 发送绑定队列为：" + delayQueue + "，转发队列为：" + ttlQueue+ "，发送时间为：" + format);
        } catch (Exception e) {
            ret = Boolean.FALSE;
            logger.error("开始发送延时：消息 " + publishInfo + " 发送绑定队列为：" + delayQueue + "，转发队列为：" + ttlQueue);

            return ret;
        }
        return ret;
    }

    private static boolean publishMessage(String queueName, byte[] message, Channel channel, String exchangeName, String severity) {
        boolean ret = Boolean.TRUE;
        long beginTime = System.currentTimeMillis();
        String publishInfo = new String(message);
        logger.debug("开始发送：消息 " + publishInfo + " 发送的选择器为：" + exchangeName + "，选择键为：" + severity+ "，选择队列为：" + queueName);
        try {
            AMQP.BasicProperties properties = MessageProperties.PERSISTENT_TEXT_PLAIN;  //设置信息持久化
            channel.queueDeclare(queueName, true, false, false, null);
            channel.basicPublish("", queueName, properties, message);
            logger.debug("发送成功：消息 " + (new String(message)) + " 发送的选择器为：" + exchangeName + "，选择键为：" + severity+ "，选择队列为：" + queueName);
        } catch (Exception e) {
            ret = Boolean.FALSE;
            logger.error("消息 " + publishInfo + " 发送的选择器为：" + exchangeName + "，选择键为：" + severity + "，发送异常：" + e.getMessage() + " 消息记录数据库！！！", e);
            // insertErrorToDb(type, message, exchangeName, severity, e);
            // String pName = ManagementFactory.getRuntimeMXBean().getName();
            //发送告警信息 todo
            long endTime = System.currentTimeMillis();
            logger.debug("发送MQ消息总耗时" + (endTime - beginTime) + "毫秒！！！");

            logger.debug("发送MQ消息总耗时" + (endTime - beginTime) + "毫秒！！！");
            return ret;
        }
        return ret;
    }


    public static boolean publishMessage(String queueName, String msg, Channel channel) {

        boolean ret = Boolean.TRUE;
        long beginTime = System.currentTimeMillis();
        logger.debug("开始发送：消息 " + msg + " 发送的队列为为：" + queueName );
        try {
            AMQP.BasicProperties properties = MessageProperties.PERSISTENT_TEXT_PLAIN;  //设置信息持久化
            channel.queueDeclare(queueName, true, false, false, null);
            channel.basicPublish("", queueName, properties, msg.getBytes());
            System.out.println("线程名："+Thread.currentThread().getName());
            logger.debug("发送成功：消息 "  + msg + " 发送的队列为为：" + queueName );
        } catch (Exception e) {
            ret = Boolean.FALSE;
            logger.error("发送：消息 "  + msg + "异常 发送的队列为为：" + queueName, e);
            // insertErrorToDb(type, message, exchangeName, severity, e);
            // String pName = ManagementFactory.getRuntimeMXBean().getName();
            //发送告警信息 todo
            long endTime = System.currentTimeMillis();
            logger.debug("发送MQ消息总耗时" + (endTime - beginTime) + "毫秒！！！");
            logger.debug("发送MQ消息总耗时" + (endTime - beginTime) + "毫秒！！！");
            return ret;
        }
        return ret;
    }

    public static void ackMessage(Long deliveryTag, Channel channel) throws IOException {
        channel.basicAck(deliveryTag, false);
    }


}

