package com.medusa.gruul.goods.mq;

import cn.hutool.json.JSONUtil;
import com.medusa.gruul.goods.api.constant.GoodsConstant;
import com.medusa.gruul.goods.api.constant.GoodsMyQueneName;
import com.medusa.gruul.goods.api.constant.GoodsQueueEnum;
import com.medusa.gruul.goods.api.constant.ShoppingCartMyQueneName;
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
 * @Author: lcysike
 * @Date: 2020/04/21 12:01
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
            log.info("消息：{} 发送失败, 应答码：{} 原因：{} 交换机: {}  路由键: {}", JSONUtil.parse(message), replyCode, replyText, exchange, routingKey);
        });
        return rabbitTemplate;
    }

    @Bean
    public Queue generateShoppingCartQueue() {
        return new Queue(ShoppingCartMyQueneName.GENERATE_SHOPPING_CART, true);
    }


    @Bean
    public Queue defaultGoodsCreateQueue() {
        return new Queue(GoodsMyQueneName.GENERATE_DEFAULT_GOODS, true);
    }

    /**
     * 商品消息实际消费队列所绑定的交换机
     */
    @Bean
    DirectExchange goodsDirect() {
        return (DirectExchange) ExchangeBuilder
                .directExchange(GoodsConstant.EXCHANGE_NAME)
                .durable(true)
                .build();
    }

    /**
     * 将商品创建队列绑定到交换机
     */
    @Bean
    Binding defaultGoodsCreateBinding(DirectExchange goodsDirect, Queue defaultGoodsCreateQueue) {
        return BindingBuilder
                .bind(defaultGoodsCreateQueue)
                .to(goodsDirect)
                .with(GoodsQueueEnum.DEFAULT_GOODS_CREATE.getRouteKey());
    }

}
