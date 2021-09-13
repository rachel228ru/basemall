package com.medusa.gruul.shops.mq;

import com.medusa.gruul.shops.api.constant.ExchangeConstant;
import com.medusa.gruul.shops.api.enums.QueueEnum;
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
			log.info("消息：{} 发送失败, 应答码：{} 原因：{} 交换机: {}  路由键: {}", correlationId, replyCode, replyText, exchange,
					routingKey);
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
	 * 用户中心初始化队列
	 */
	@Bean
	public Queue accountCenterInitQueue() {
		return new Queue(QueueEnum.QUEUE_SHOPS_CENTER_INIT.getName(), true);
	}

	/**
	 * 店铺模板初始化队列
	 */
	@Bean
	public Queue shopsTemplateInitQueue() {
		return new Queue(QueueEnum.QUEUE_SHOPS_TEMPLATE_INIT.getName(), true);
	}


	/**
	 * 用户中心初始化队列
	 */
	@Bean
	Binding orderCreateBinding(DirectExchange shopsDirect, Queue accountCenterInitQueue) {
		return BindingBuilder.bind(accountCenterInitQueue).to(shopsDirect)
				.with(QueueEnum.QUEUE_SHOPS_CENTER_INIT.getRouteKey());
	}

	/**
	 * 店铺模板初始化队列
	 */
	@Bean
	Binding shopsTemplateBinding(DirectExchange shopsDirect, Queue shopsTemplateInitQueue) {
		return BindingBuilder.bind(shopsTemplateInitQueue).to(shopsDirect)
				.with(QueueEnum.QUEUE_SHOPS_TEMPLATE_INIT.getRouteKey());
	}

	/**
	 * 店铺服务交换机
	 */
	@Bean
	DirectExchange shopsDirect() {
		return (DirectExchange) ExchangeBuilder.directExchange(ExchangeConstant.SHOPS_EXCHANGE).durable(true).build();
	}


	/**
	 *  店铺引导页初始化队列
	 */
	@Bean
	public Queue shopGuidePageInitQueue() {
		return new Queue(QueueEnum.QUEUE_SHOP_GUIDE_PAGE_INIT.getName(), true);

	}


	/**
	 * 店铺引导页初始化绑定到交换机
	 */
	@Bean
	Binding shopGuidePageInitBinding(DirectExchange shopsDirect, Queue shopGuidePageInitQueue) {
		return BindingBuilder.bind(shopGuidePageInitQueue).to(shopsDirect)
				.with(QueueEnum.QUEUE_SHOP_GUIDE_PAGE_INIT.getRouteKey());
	}


}
