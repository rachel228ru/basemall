package com.medusa.gruul.common.rabbitmq.test;

import com.medusa.gruul.common.rabbitmq.core.BaseMqPublish;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;


/**
 * @Description: mq消费测试组件
 * @Author: wangpeng
 * @Date: 2019/7/21 10:05
 */
public class DelayDoConsumer extends DefaultConsumer {

    private final static Logger logger = LoggerFactory.getLogger(BaseMqPublish.class);

    private static AtomicLong count = new AtomicLong(0);

    protected Channel channel;

    public DelayDoConsumer(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        String message = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
            String format = simpleDateFormat.format(new Date());
            message = new String(body, "UTF-8");
            System.out.println("获取延时消息：received message:" + message + ",接收时间:" + format);
        } catch (Exception e) {
            logger.error("线程号：" + Thread.currentThread().getId() + "消费错误：" + e.getMessage(), e);
        } finally {
            logger.debug("线程号：" + Thread.currentThread().getId() + "回复MQ");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            BaseMqPublish.ackMessage(envelope.getDeliveryTag(), channel);
            logger.info("总消费条数：" + count.incrementAndGet());
        }
    }
}
