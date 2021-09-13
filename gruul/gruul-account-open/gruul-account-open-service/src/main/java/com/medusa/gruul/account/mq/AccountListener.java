package com.medusa.gruul.account.mq;

import cn.hutool.core.util.ObjectUtil;
import com.medusa.gruul.account.api.constant.AccountQueueNameConstant;
import com.medusa.gruul.account.service.*;
import com.medusa.gruul.order.api.model.OrderVo;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author whh
 * @description 用户队列监听
 * @data: 2019/12/11
 */
@Slf4j
@Component
public class AccountListener {

    @Autowired
    private IMiniAccountExtendsService miniAccountExtendsService;

    @Autowired
    private IMiniAccountService miniAccountService;

    @Autowired
    private IApiMiniAccountCollectService miniAccountCollectService;




    /**
     * 订单支付成功->修改用户最后交易时间
     *
     * @param orderVo    com.medusa.gruul.order.api.model.OrderVo
     * @param properties org.springframework.amqp.core.MessageProperties
     * @param channel    com.rabbitmq.client.Channel
     * @throws IOException
     */
    @RabbitListener(queues = AccountQueueNameConstant.ACCOUNT_ORDER_PAY_OK_QUEUE_CHANGE)
    public void orderPayOk(OrderVo orderVo, MessageProperties properties, Channel channel) throws IOException {
        log.info("orderPayOk receive message:" + orderVo.toString());
        if (ObjectUtil.isNull(orderVo)) {
            //手动确认
            channel.basicAck(properties.getDeliveryTag(), true);
            return;
        }
        miniAccountExtendsService.modifyLastDealTime(orderVo.getUserId(), orderVo.getPayTime());
        //手动确认
        channel.basicAck(properties.getDeliveryTag(), true);
    }


    /**
     * 订单完成通知;指定用户增加消费次数和消费金额
     *
     * @param orderVo    com.medusa.gruul.order.api.model.OrderVo
     * @param properties org.springframework.amqp.core.MessageProperties
     * @param channel    com.rabbitmq.client.Channel
     * @throws IOException
     */
    @RabbitListener(queues = AccountQueueNameConstant.ACCOUNT_ORDER_COMPLETE_OK_QUEUE_CHANGE)
    public void orderCompleted(OrderVo orderVo, MessageProperties properties, Channel channel) throws IOException {
        log.info("completed receive message:" + orderVo.toString());
        if (ObjectUtil.isNull(orderVo)) {
            //手动确认
            channel.basicAck(properties.getDeliveryTag(), true);
            return;
        }

        miniAccountExtendsService.orderCompleted(orderVo);
        //手动确认
        channel.basicAck(properties.getDeliveryTag(), true);
    }

    /**
     * 更新用户收藏 sql表数据
     *
     * @param collectMessage
     * @param properties
     * @param channel
     */
    @RabbitListener(queues = AccountQueueNameConstant.ACCOUNT_COLLECT)
    public void userCollect(CollectMessage collectMessage, MessageProperties properties, Channel channel) throws IOException {
        log.info(" receive message:" + collectMessage.toString());
        //更新或插入 用户收藏数据
        miniAccountCollectService.updateAccountCollect(collectMessage);
    }


    /**
     * 生成默认账号
     *
     * @param jsonData json字符串
     */
    @RabbitListener(queues = AccountQueueNameConstant.ACCOUNT_DEFAULT)
    public void accountDefault(String jsonData) {
        log.info(" receive message:" + jsonData);
        miniAccountService.generateAccountDefault(jsonData);
    }

}
