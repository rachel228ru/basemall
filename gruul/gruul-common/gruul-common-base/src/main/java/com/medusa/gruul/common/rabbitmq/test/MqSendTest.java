package com.medusa.gruul.common.rabbitmq.test;

import com.medusa.gruul.common.rabbitmq.core.MqSend;

/**
 * @Description: 基础mq组件测试类
 * @Author: wangpeng
 * @Date: 2019/7/21 10:05
 */
public class MqSendTest {

    public static void main(String[] args) throws Exception {
       /* for (int i = 0; i <30 ; i++) {
            send();
        }*/
        send();

    }

    private static void send(){

        MqSend testSendTask = new MqSend();

        /**
         * 发送消息
         * @param exchangeName 交换机名称
         * @param severity 路由key
         * @param msg 转发的消息
         */
        //
      testSendTask.executeSend(  "99999111999999" , "22222","111111","OTHER_QUEUE");

        //testSendTask.executeSendQueue("FL_SLOW_QUE","test888");

    }


}
