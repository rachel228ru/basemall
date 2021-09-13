package com.medusa.gruul.payment.mq;

import com.alibaba.fastjson.JSONObject;
import com.medusa.gruul.payment.api.constant.PaymentQueueNameConstant;
import com.medusa.gruul.payment.api.entity.Payment;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Description: Receiver.java
 * @Author: alan
 * @Date: 2019/10/6 13:49
 */
@Slf4j
@Component
public class PaymentListener {


    @RabbitListener(queues = PaymentQueueNameConstant.PAYMENT_MINI_OK_NOTIFY)
    public void receive(Payment orderMessage, MessageProperties properties, Channel channel) {


    }

}

