package com.medusa.gruul.order.mq;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;


/**
 * 设置消息的失效时间
 *
 * @author alan
 * @date 2019/12/7 9:47
 */
public class ExpirationMessagePostProcessor implements MessagePostProcessor {
    /**
     * 毫秒
     */
    private final Long ttl;

    public ExpirationMessagePostProcessor(Long ttl) {
        if (ttl == 0) {
            ttl = 10L;
        }
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