package com.medusa.gruul.goods.mq;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;


/**
 * @description: 设置消息的失效时间
 * @author: lcysike
 * @date: 2020/04/21 12:47
 */
public class ExpirationMessagePostProcessor implements MessagePostProcessor {
    /**
     * 毫秒
     */
    private final Long ttl;

    public ExpirationMessagePostProcessor(Long ttl) {
        this.ttl = ttl;
    }

    @Override
    public Message postProcessMessage(Message message) throws AmqpException {
        //设置per-message的失效时间
        message.getMessageProperties()
                .setDelay(ttl.intValue());
        return message;
    }
}