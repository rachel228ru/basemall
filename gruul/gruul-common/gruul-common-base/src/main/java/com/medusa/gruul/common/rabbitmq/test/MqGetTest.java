package com.medusa.gruul.common.rabbitmq.test;

import com.medusa.gruul.common.rabbitmq.core.MqConsumer;

/**
 * @Description: 基础mq组件测试类
 * @Author: wangpeng
 * @Date: 2019/7/21 10:05
 */
public class MqGetTest {

    public static void main(String[] args) throws Exception {
       /* for (int i = 0; i <30 ; i++) {
            MqSend testSendTask = new MqSend();
            testSendTask.executeSend(  "test1111" , MdsConstant.MQ_FL_EXCHANGE,MdsConstant.MQ_FL_SLOW_SEVERITIE);
        }*/
        get();
       /* Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        for (Thread thread : threadSet) {
            System.out.println(thread.getId());
        }*/

    }



    private static void get(){
        MqConsumer testConsumerTask = new MqConsumer();
         testConsumerTask.execute("OTHER_QUEUE","222222", "111111",TestConsumer.class);



    }


}
