package com.medusa.gruul.payment.mq;

import com.medusa.gruul.payment.api.constant.PaymentExchangeConstant;
import com.medusa.gruul.payment.api.constant.PaymentQueueNameConstant;
import com.medusa.gruul.payment.api.enums.PaymentQueueEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;

import javax.annotation.Resource;

/**
 * @Description: RabbitConfig.java
 * @Author: alan
 * @Date: 2019/10/6 14:01
 */
@Slf4j
@Configuration
public class RabbitConfig implements RabbitListenerConfigurer {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
    }

    @Bean
    MessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory messageHandlerMethodFactory = new DefaultMessageHandlerMethodFactory();
        messageHandlerMethodFactory.setMessageConverter(consumerJackson2MessageConverter());
        return messageHandlerMethodFactory;
    }

    @Bean
    public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
        return new MappingJackson2MessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate() {
        // 使用jackson 消息转换器
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.setEncoding("UTF-8");
        // 消息发送失败返回到队列中，yml需要配置 publisher-returns: true
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            String correlationId = message.getMessageProperties().getCorrelationId();
            log.info("消息：{} 发送失败, 应答码：{} 原因：{} 交换机: {}  路由键: {}", correlationId, replyCode, replyText, exchange, routingKey);
        });
        // 消息确认，yml需要配置 publisher-confirms: true
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.info("消息发送到exchange成功,id: {}", correlationData.getId());
            } else {
                log.info("消息发送到exchange失败,原因: {}", cause);
            }
        });
        return rabbitTemplate;
    }

    /**
     * 支付回调服务交换机
     */
    @Bean
    DirectExchange paymentDirect() {
        return (DirectExchange) ExchangeBuilder
                .directExchange(PaymentExchangeConstant.PAYMENT_EXCHANGE)
                .durable(true)
                .build();
    }

    /**
     * 小程序支付回调队列
     */
    @Bean
    public Queue  miniNotifyQueue() {
        return new Queue(PaymentQueueNameConstant.PAYMENT_MINI_OK_NOTIFY, true);
    }


    /**
     * 将回调队列绑定到支付交换机
     */
    @Bean
    Binding orderCreateBinding(DirectExchange paymentDirect, Queue miniNotifyQueue) {
        return BindingBuilder
                .bind(miniNotifyQueue)
                .to(paymentDirect)
                .with(PaymentQueueEnum.QUEUE_PAYMENT_NOTIFY_SUCCESS.getRouteKey());
    }


}
