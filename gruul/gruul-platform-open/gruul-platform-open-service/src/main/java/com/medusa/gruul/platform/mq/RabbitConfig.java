package com.medusa.gruul.platform.mq;

import com.medusa.gruul.platform.api.constant.ExchangeConstant;
import com.medusa.gruul.platform.api.enums.QueueEnum;
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
import java.util.HashMap;
import java.util.Map;

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
     * 平台服务交换机
     */
    @Bean
    DirectExchange platformDirect() {
        return (DirectExchange) ExchangeBuilder
                .directExchange(ExchangeConstant.PLATFORM_EXCHANGE)
                .durable(true)
                .build();
    }


    /**
     * 订阅消息发送队列
     */
    @Bean
    public Queue subscribeMsgSendQueue() {
        return new Queue(QueueEnum.PLATFORM_SUBSCRIBE_MSG_SEND.getName(), true);
    }


    /**
     * 订阅消息发送队列绑定交换机
     */
    @Bean
    Binding subscribeMsgSendBinding(DirectExchange platformDirect, Queue subscribeMsgSendQueue) {
        return BindingBuilder
                .bind(subscribeMsgSendQueue)
                .to(platformDirect)
                .with(QueueEnum.PLATFORM_SUBSCRIBE_MSG_SEND.getRouteKey());
    }


    /**
     * 延迟队列队列所绑定的交换机
     */
    @Bean
    CustomExchange platefromDelayDirect() {
        Map<String, Object> args = new HashMap<>(1);
        args.put("x-delayed-type", "direct");
        return new CustomExchange(ExchangeConstant.PLATFORM_DELAY_EXCHANGE, "x-delayed-message", true, false, args);
    }


    @Bean
    public Queue packageDueQueue() {
        return new Queue(QueueEnum.PLATFORM_PACKAGE_DUE.getName());
    }

    @Bean
    public Binding bindingNotify(Queue packageDueQueue, CustomExchange platefromDelayDirect) {
        return BindingBuilder.bind(packageDueQueue).to(platefromDelayDirect).with(QueueEnum.PLATFORM_PACKAGE_DUE.getRouteKey()).noargs();
    }

}
