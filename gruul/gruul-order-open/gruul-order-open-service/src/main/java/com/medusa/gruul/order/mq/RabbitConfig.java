package com.medusa.gruul.order.mq;

import cn.hutool.json.JSONUtil;
import com.medusa.gruul.account.api.constant.AccountExchangeConstant;
import com.medusa.gruul.order.api.constant.OrderConstant;
import com.medusa.gruul.order.api.constant.OrderQueueEnum;
import com.medusa.gruul.order.api.constant.OrderQueueNameConstant;
import com.medusa.gruul.payment.api.constant.PaymentExchangeConstant;
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
 * RabbitConfig.java
 *
 * @author alan
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
                log.info("消息发送到exchange成功,id: {}", correlationData.getId());
            } else {
                log.info("消息发送到exchange失败,原因: {}", cause);
            }
        });
        return rabbitTemplate;
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

    /**
     * 订单延迟队列队列所绑定的交换机
     */
    @Bean
    CustomExchange orderDelayDirect() {
        Map<String, Object> args = new HashMap<>(1);
        args.put("x-delayed-type", "direct");
        return new CustomExchange(OrderConstant.DELAY_EXCHANGE_NAME, "x-delayed-message", true, false, args);
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
     * 账户服务交换机
     */
    @Bean
    DirectExchange accountDirect() {
        return (DirectExchange) ExchangeBuilder
                .directExchange(AccountExchangeConstant.ACCOUNT_EXCHANGE)
                .durable(true)
                .build();
    }

    @Bean
    public Queue orderCreateQueue() {
        return new Queue(OrderQueueNameConstant.ORDER_CREATE, true);
    }

    @Bean
    public Queue orderCreateFailQueue() {
        return new Queue(OrderQueueNameConstant.ORDER_CREATE_FAIL, true);
    }

    @Bean
    public Queue orderPayedQueue() {
        return new Queue(OrderQueueNameConstant.ORDER_PAYED, true);
    }

    @Bean
    public Queue orderReceiptQueue() {
        return new Queue(OrderQueueNameConstant.ORDER_RECEIPT, true);
    }

    @Bean
    public Queue orderAutoReceiptQueue() {
        return new Queue(OrderQueueNameConstant.ORDER_AUTO_RECEIPT, true);
    }

    @Bean
    public Queue orderAutoCompletedQueue() {
        return new Queue(OrderQueueNameConstant.ORDER_AUTO_COMPLETED, true);
    }

    @Bean
    public Queue orderCompletedQueue() {
        return new Queue(OrderQueueNameConstant.ORDER_COMPLETED, true);
    }

    @Bean
    public Queue deliverRemoveQueue() {
        return new Queue(OrderQueueNameConstant.DELIVER_REMOVE, true);
    }

    @Bean
    public Queue orderCancelQueue() {
        return new Queue(OrderQueueNameConstant.ORDER_AUTO_CANCEL, true);
    }

    @Bean
    public Queue orderCloseQueue() {
        return new Queue(OrderQueueNameConstant.ORDER_CLOSE, true);
    }

    @Bean
    public Queue orderReturnQueue() {
        return new Queue(OrderQueueNameConstant.ORDER_RETURN, true);
    }


    @Bean
    public Queue orderCancelFailQueue() {
        return new Queue(OrderQueueNameConstant.ORDER_CANCEL_FAIL, true);
    }


    @Bean
    public Queue deliverCreateQueue() {
        return new Queue(OrderQueueNameConstant.DELIVER_CREATE, true);
    }

    @Bean
    public Queue refundNotifyQueue() {
        return new Queue(OrderQueueNameConstant.REFUND_NOTIFY, true);
    }

    @Bean
    public Queue refundNotifySucceedQueue() {
        return new Queue(OrderQueueNameConstant.REFUND_NOTIFY_SUCCEED, true);
    }


    /**
     * 将物流创建队列绑定到交换机
     */
    @Bean
    Binding deliverCreateBinding(DirectExchange deliverDirect, Queue deliverCreateQueue) {
        return BindingBuilder
                .bind(deliverCreateQueue)
                .to(deliverDirect)
                .with(OrderQueueEnum.QUEUE_DELIVER_CREATE.getRouteKey());
    }

    @Bean
    public Queue deliverReceiptQueue() {
        return new Queue(OrderQueueNameConstant.DELIVER_RECEIPT, true);
    }

    @Bean
    public Queue paymentNotifyQueue() {
        return new Queue(OrderQueueNameConstant.PAYMENT_NOTIFY, true);
    }

    @Bean
    public Queue orderDataInitQueue() {
        return new Queue(OrderQueueNameConstant.DATA_INIT, true);
    }

    /**
     * 将物流签收队列绑定到交换机
     */
    @Bean
    Binding deliverReceiptBinding(DirectExchange deliverDirect, Queue deliverReceiptQueue) {
        return BindingBuilder
                .bind(deliverReceiptQueue)
                .to(deliverDirect)
                .with(OrderQueueEnum.QUEUE_DELIVER_RECEIPT.getRouteKey());
    }


    /**
     * 将订单创建队列绑定到交换机
     */
    @Bean
    Binding orderCreateBinding(DirectExchange orderDirect, Queue orderCreateQueue) {
        return BindingBuilder
                .bind(orderCreateQueue)
                .to(orderDirect)
                .with(OrderQueueEnum.QUEUE_ORDER_CREATE.getRouteKey());
    }

    /**
     * 将订单创建失败队列绑定到交换机
     */
    @Bean
    Binding orderCreateFailBinding(DirectExchange orderDirect, Queue orderCreateFailQueue) {
        return BindingBuilder
                .bind(orderCreateFailQueue)
                .to(orderDirect)
                .with(OrderQueueEnum.QUEUE_ORDER_CREATE_FAIL.getRouteKey());
    }

    /**
     * 将订单支付队列绑定到交换机
     */
    @Bean
    Binding orderPayedBinding(DirectExchange orderDirect, Queue orderPayedQueue) {
        return BindingBuilder
                .bind(orderPayedQueue)
                .to(orderDirect)
                .with(OrderQueueEnum.QUEUE_ORDER_PAYED.getRouteKey());
    }

    /**
     * 将订单签收队列绑定到交换机
     */
    @Bean
    Binding orderReceiptBinding(DirectExchange orderDirect, Queue orderReceiptQueue) {
        return BindingBuilder
                .bind(orderReceiptQueue)
                .to(orderDirect)
                .with(OrderQueueEnum.QUEUE_ORDER_RECEIPT.getRouteKey());
    }

    /**
     * 将订单完成队列绑定到交换机
     */
    @Bean
    Binding orderCompletedBinding(DirectExchange orderDirect, Queue orderCompletedQueue) {
        return BindingBuilder
                .bind(orderCompletedQueue)
                .to(orderDirect)
                .with(OrderQueueEnum.QUEUE_ORDER_COMPLETED.getRouteKey());
    }

    @Bean
    Binding refundNotifySucceedBinding(DirectExchange orderDirect, Queue refundNotifySucceedQueue) {
        return BindingBuilder
                .bind(refundNotifySucceedQueue)
                .to(orderDirect)
                .with(OrderQueueEnum.QUEUE_ORDER_RETURN_SUCCEED.getRouteKey());
    }

    /**
     * 将订单关闭队列绑定到交换机
     */
    @Bean
    Binding orderCloseBinding(DirectExchange orderDirect, Queue orderCloseQueue) {
        return BindingBuilder
                .bind(orderCloseQueue)
                .to(orderDirect)
                .with(OrderQueueEnum.QUEUE_ORDER_CLOSE.getRouteKey());
    }

    /**
     * 将退款订单队列绑定到交换机
     */
    @Bean
    Binding orderReturnBinding(DirectExchange orderDirect, Queue orderReturnQueue) {
        return BindingBuilder
                .bind(orderReturnQueue)
                .to(orderDirect)
                .with(OrderQueueEnum.QUEUE_ORDER_RETURN.getRouteKey());
    }

    /**
     * 将订单自动完成队列绑定到交换机
     */
    @Bean
    Binding orderAutoCompletedBinding(CustomExchange orderDelayDirect, Queue orderAutoCompletedQueue) {
        return BindingBuilder
                .bind(orderAutoCompletedQueue)
                .to(orderDelayDirect)
                .with(OrderQueueEnum.QUEUE_ORDER_AUTO_COMPLETED.getRouteKey()).noargs();
    }

    /**
     * 将订单自动确认收货队列绑定到交换机
     */
    @Bean
    Binding orderAutoReceiptBinding(CustomExchange orderDelayDirect, Queue orderAutoReceiptQueue) {
        return BindingBuilder
                .bind(orderAutoReceiptQueue)
                .to(orderDelayDirect)
                .with(OrderQueueEnum.QUEUE_ORDER_AUTO_RECEIPT.getRouteKey()).noargs();
    }

    /**
     * 将订单自动取消队列绑定到交换机
     */
    @Bean
    Binding orderAutoCancelBinding(CustomExchange orderDelayDirect, Queue orderCancelQueue) {
        return BindingBuilder
                .bind(orderCancelQueue)
                .to(orderDelayDirect)
                .with(OrderQueueEnum.QUEUE_ORDER_AUTO_CANCEL.getRouteKey()).noargs();
    }

    /**
     * 将订单取消失败队列绑定到交换机
     */
    @Bean
    Binding orderCancelFailBinding(CustomExchange orderDelayDirect, Queue orderCancelFailQueue) {
        return BindingBuilder
                .bind(orderCancelFailQueue)
                .to(orderDelayDirect)
                .with(OrderQueueEnum.QUEUE_ORDER_CANCEL_FAIL.getRouteKey()).noargs();
    }


    /**
     * 将移出发货单队列绑定到交换机
     */
    @Bean
    Binding deliverRemoveBinding(DirectExchange orderDirect, Queue deliverRemoveQueue) {
        return BindingBuilder
                .bind(deliverRemoveQueue)
                .to(orderDirect)
                .with(OrderQueueEnum.QUEUE_DELIVER_REMOVE.getRouteKey());
    }


    /**
     * 将订单支付成功绑定到支付模块交换机
     */
    @Bean
    Binding paymentNotifyBinding(DirectExchange paymentDirect, Queue paymentNotifyQueue) {
        return BindingBuilder
                .bind(paymentNotifyQueue)
                .to(paymentDirect)
                .with(OrderQueueEnum.QUEUE_ORDER_PAYMENT_NOTIFY.getRouteKey());
    }

    /**
     * 将退款成功绑定到支付模块交换机
     */
    @Bean
    Binding refundNotifyBinding(DirectExchange paymentDirect, Queue refundNotifyQueue) {
        return BindingBuilder
                .bind(refundNotifyQueue)
                .to(paymentDirect)
                .with(OrderQueueEnum.QUEUE_ORDER_REFUND_NOTIFY.getRouteKey());
    }




    /**
     * 将订单数据初始化绑定到控制台模块
     */
    @Bean
    Binding orderDataInitBinding(DirectExchange accountDirect, Queue orderDataInitQueue) {
        return BindingBuilder
                .bind(orderDataInitQueue)
                .to(accountDirect)
                .with(OrderQueueEnum.QUEUE_DATA_INIT.getRouteKey());
    }


}
