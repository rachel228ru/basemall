package com.medusa.gruul.common.rabbitmq.test;

import com.medusa.gruul.common.rabbitmq.core.MqSend;
/**
 * @author wangpeng
 * @data 2018-07-08下午1:41:10
 * @description TODO
 * @version V1.0
 */
public class DelaySendTest {
    public static void main(String[] args) {

      /*  String delayQueue ="DELAY_QUEUE";
        String expires ="10000";
        String ttlQueue ="TTL_QUEUE";*/
            //DELAY_QUEUE
            MqSend testSendTask = new MqSend();

            testSendTask.executeDLSend("DELAY_QUEUE","TTL_QUEUE","test999","10000");


    }
}
