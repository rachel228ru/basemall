package com.medusa.gruul.common.rabbitmq.test;

import com.medusa.gruul.common.rabbitmq.core.MqConsumer;
/**
 * @author wangpeng
 * @data 2018-07-08下午1:41:10
 * @description
 * @version V1.0
 */
public class DelayConsumerTest {
    public static void main(String[] args) {
        MqConsumer testConsumerTask = new MqConsumer();
        testConsumerTask.executeDl("TTL_QUEUE","DELAY_QUEUE",DelayDoConsumer.class);
    }
}
