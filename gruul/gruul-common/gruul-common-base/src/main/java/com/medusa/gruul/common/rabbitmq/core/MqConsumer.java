package com.medusa.gruul.common.rabbitmq.core;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: mq消费测试组件
 * @author wangpeng
 * @date 2018/8/8
 */

public class MqConsumer {
    private final static Logger logger = LoggerFactory.getLogger(BaseMqPublish.class);
    private SmsSingleMqHelper mq;
    private BaseMq baseMqUtil = null;

    public MqConsumer() {
        try {
            mq = SmsSingleMqHelper.getInstance(BaseMq.getInitedInstance(MqSelector.MDS_P));
            baseMqUtil = BaseMq.getInitedInstance(MqSelector.MDS_P);
        } catch (Exception e) {
            logger.error("创建mq生产者失败", e);
        }
    }


    public void execute(String queueName,String exchange,String routingKey,Class clazz) {
        try {
            logger.info("开始启动MQ消费进程！！！！");
            mq.initConnection(2, 3, queueName,exchange,routingKey, "direct",null);
            mq.startConsume(clazz.getName(),false);
        } catch (Exception e) {
            logger.error("MQ 重联进程启动失败，" + e.getMessage(), e);
            this.close();
        }
    }

    public void executeDl(String ttlQueue,String delayQueue,Class clazz) {
        try {
            logger.info("开始启动MQ延时消息消费进程！！！！");
            mq.initConnection(2, 3, ttlQueue,ttlQueue, null, "direct",delayQueue);
            mq.startConsume(clazz.getName(),true);
        } catch (Exception e) {
            logger.error("MQ 重联进程启动失败，" + e.getMessage(), e);
            this.close();
        }
    }
    private boolean close() {
        try {
            logger.info("开始释放MQ 重联进程资源！！！！");
            mq.shutdown();
            baseMqUtil.destroy();
        } catch (Exception e) {
            logger.error("MQ 重联进程资源释放失败，" + e.getMessage(), e);
            e.printStackTrace();
        }
        return true;
    }
}

