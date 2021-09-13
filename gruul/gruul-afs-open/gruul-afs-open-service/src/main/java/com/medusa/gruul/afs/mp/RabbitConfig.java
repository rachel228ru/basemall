package com.medusa.gruul.afs.mp;

import cn.hutool.json.JSONUtil;
import com.medusa.gruul.account.api.constant.AccountExchangeConstant;
import com.medusa.gruul.afs.api.constant.AfsConstant;
import com.medusa.gruul.afs.api.constant.AfsQueueEnum;
import com.medusa.gruul.afs.api.constant.AfsQueueNameConstant;
import com.medusa.gruul.order.api.constant.OrderConstant;
import com.medusa.gruul.order.api.constant.OrderQueueEnum;
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
 * @author alan
 * @Description: RabbitConfig.java
 * @date 2019/10/6 14:01
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
            log.info("消息：{} 发送失败, 应答码：{} 原因：{} 交换机: {}  路由键: {}", JSONUtil.parse(message), replyCode, replyText,
                    exchange, routingKey);
        });
        // 消息确认，yml需要配置 publisher-confirms: true
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                assert correlationData != null;
                log.info("消息发送到exchange成功,id: {}", correlationData.getId());
            } else {
                log.info("消息发送到exchange失败,原因: {}", cause);
            }
        });
        return rabbitTemplate;
    }

    /**
     * 售后延迟队列队列所绑定的交换机
     */
    @Bean
    CustomExchange afsDelayDirect() {
        Map<String, Object> args = new HashMap<>(1);
        args.put("x-delayed-type", "direct");
        return new CustomExchange(AfsConstant.DELAY_EXCHANGE_NAME, "x-delayed-message", true, false, args);
    }

    /**
     * 订单消息实际消费队列所绑定的交换机
     */
    @Bean
    DirectExchange orderDirect() {
        return (DirectExchange) ExchangeBuilder
                .directExchange(OrderConstant.EXCHANGE_NAME)
                .durable(true)
                .build();
    }

    /**
     * 物流消息实际消费队列所绑定的交换机
     */
    @Bean
    DirectExchange deliverDirect() {
        return (DirectExchange) ExchangeBuilder
                .directExchange(OrderConstant.DELIVER_EXCHANGE_NAME)
                .durable(true)
                .build();
    }



    @Bean
    DirectExchange accountDirect() {
        return (DirectExchange) ExchangeBuilder
                .directExchange(AccountExchangeConstant.ACCOUNT_EXCHANGE)
                .durable(true)
                .build();
    }

    @Bean
    public Queue orderReceipt() {
        return new Queue(AfsQueueNameConstant.ORDER_RECEIPT, true);
    }

    @Bean
    public Queue orderShipped() {
        return new Queue(AfsQueueNameConstant.ORDER_SHIPPED, true);
    }

    @Bean
    public Queue orderCompleted() {
        return new Queue(AfsQueueNameConstant.ORDER_COMPLETED, true);
    }

    @Bean
    public Queue deliverReceipt() {
        return new Queue(AfsQueueNameConstant.DELIVER_RECEIPT, true);
    }

    @Bean
    public Queue afsMerchantAutoConfirm() {
        return new Queue(AfsQueueNameConstant.AFS_MERCHANT_AUTO_CONFIRM, true);
    }

    @Bean
    public Queue afsUserReturnOvertime() {
        return new Queue(AfsQueueNameConstant.AFS_USER_RETURN_OVERTIME, true);
    }

    @Bean
    public Queue afsDataInitQueue() {
        return new Queue(AfsQueueNameConstant.DATA_INIT, true);
    }


    /**
     * 将商家售后自动确认队列绑定到交换机
     */
    @Bean
    Binding merchantAutoCancelBinding(CustomExchange afsDelayDirect, Queue afsMerchantAutoConfirm) {
        return BindingBuilder
                .bind(afsMerchantAutoConfirm)
                .to(afsDelayDirect)
                .with(AfsQueueEnum.QUEUE_AFS_MERCHANT_AUTO_CONFIRM.getRouteKey()).noargs();
    }


    /**
     * 将用户超时未退货自动取消队列绑定到交换机
     */
    @Bean
    Binding userReturnOvertimeBinding(CustomExchange afsDelayDirect, Queue afsUserReturnOvertime) {
        return BindingBuilder
                .bind(afsUserReturnOvertime)
                .to(afsDelayDirect)
                .with(AfsQueueEnum.QUEUE_AFS_USER_RETURN_OVERTIME.getRouteKey()).noargs();
    }


    /**
     * 将物流签收队列绑定到交换机
     */
    @Bean
    Binding deliverReceiptBinding(DirectExchange deliverDirect, Queue deliverReceipt) {
        return BindingBuilder
                .bind(deliverReceipt)
                .to(deliverDirect)
                .with(OrderQueueEnum.QUEUE_DELIVER_RECEIPT.getRouteKey());
    }

    /**
     * 将订单签收队列绑定到交换机
     */
    @Bean
    Binding orderReceiptBinding(DirectExchange orderDirect, Queue orderReceipt) {
        return BindingBuilder
                .bind(orderReceipt)
                .to(orderDirect)
                .with(OrderQueueEnum.QUEUE_ORDER_RECEIPT.getRouteKey());
    }

    /**
     * 将订单发货队列绑定到交换机
     */
    @Bean
    Binding orderShippedBinding(DirectExchange orderDirect, Queue orderShipped) {
        return BindingBuilder
                .bind(orderShipped)
                .to(orderDirect)
                .with(OrderQueueEnum.QUEUE_ORDER_SHIPPED.getRouteKey());
    }

    /**
     * 将订单评价队列绑定到交换机
     */
    @Bean
    Binding orderCompletedBinding(DirectExchange orderDirect, Queue orderCompleted) {
        return BindingBuilder
                .bind(orderCompleted)
                .to(orderDirect)
                .with(OrderQueueEnum.QUEUE_ORDER_COMPLETED.getRouteKey());
    }


    /**
     * 将数据初始化绑定到控制台模块
     */
    @Bean
    Binding orderDataInitBinding(DirectExchange accountDirect, Queue afsDataInitQueue) {
        return BindingBuilder
                .bind(afsDataInitQueue)
                .to(accountDirect)
                .with(AfsQueueEnum.QUEUE_DATA_INIT.getRouteKey());
    }

}
