package com.medusa.gruul.common.rabbitmq.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;


/**
 /**
 * @Description: mq消费组件
 * @author wangpeng
 * @date 2018/8/8
 */

public class MqSend {
    private final static Logger logger = LoggerFactory.getLogger(BaseMqPublish.class);
    private AtomicLong mobile = new AtomicLong(13716555328L);//系统告警手机号
    private AtomicLong count = new AtomicLong(0);
    private BaseMq baseMqUtil = null;

    public MqSend() {
        try {
            baseMqUtil = BaseMq.getInitedInstance(MqSelector.MDS_P);
        } catch (Exception e) {
            logger.error(e.getMessage());
            System.exit(0);
        }
    }

    /**
     * 发送消息
     * @param exchangeName 交换机名称
     * @param severity 路由key
     * @param msg 转发的消息
     */
    public void executeSend(String msg,String exchangeName, String severity,String queueName) {
        logger.info("开始发送消息mq！！！！");

            try {
                BaseMqPublish.publishMessage(msg.getBytes(), MqSelector.MDS_P,exchangeName, severity,queueName);
                count.incrementAndGet();
                logger.info("当前导入mq数" + count.get());

            } catch (Exception e) {
                logger.error("导入mq进程启动失败！！！！", e);
            }

         this.close();

    }


    /**
     * 发送消息
     * @param queueName 队列名称
     * @param msg 消息
     */
    public void executeSendQueue(String queueName,String msg) {
        logger.info("开始发送消息mq！！！！");

        try {
            BaseMqPublish.publishMessage(queueName,msg,MqSelector.MDS_P);
        } catch (Exception e) {
            logger.error("导入mq进程启动失败！！！！", e);
        }

        this.close();

    }

    /**
     * 发送消息
     * @param delayQueue 延时队列
     * @param ttlQueue 延时转发接收的队列
     * @param msg 转发的消息
     * @param expires 过期时间
     */
    public void executeDLSend(String delayQueue,String ttlQueue,String msg,String expires) {
        logger.info("开始发送延时消息mq！！！！");
            try {
                BaseMqPublish.publishDlMsg(msg.getBytes(), MqSelector.DL_P, delayQueue,ttlQueue,expires);
                count.incrementAndGet();
                logger.info("当前发送mq数" + count.get());
            } catch (Exception e) {
                logger.error("导入mq进程启动失败！！！！", e);
            }
        this.close();

    }


    private boolean close() {
        try {
            baseMqUtil.destroy();
            logger.info("当前总导入mq数" + count.get());
        } catch (Exception e) {
            logger.error("关闭mq出错", e);
        }
        return  true;
    }
}

