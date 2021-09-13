package com.medusa.gruul.afs.mp;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.medusa.gruul.afs.api.constant.AfsQueueNameConstant;
import com.medusa.gruul.afs.mp.model.BaseAfsOrderMessage;
import com.medusa.gruul.afs.service.IAfsOrderService;
import com.medusa.gruul.afs.service.IAfsReasonService;
import com.medusa.gruul.afs.service.IManageAfsOrderService;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.order.api.constant.OrderQueueNameConstant;
import com.medusa.gruul.order.api.model.DataInitMessage;
import com.medusa.gruul.order.api.model.OrderVo;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author alan
 * @Description: Receiver.java
 * @date 2019/10/6 13:49
 */
@Slf4j
@Component
public class AfsListener {
    @Resource
    private IManageAfsOrderService manageAfsOrderService;
    @Resource
    private IAfsOrderService afsOrderService;
    @Resource
    private IAfsReasonService afsReasonService;


    /**
     * 数据初始化
     *
     * @param jsonStr
     * @return void
     * @author alan
     * @date 2019/12/9 20:20
     */
    @RabbitListener(queues = OrderQueueNameConstant.DATA_INIT)
    public void dataInit(String jsonStr, Channel channel, Message m) throws IOException {
        log.info("收到数据初始化消息:deliveryTag{},当前时间{},消息内容{}.", m.getMessageProperties().getDeliveryTag(), DateUtil.now(),
                jsonStr);
        try {
            DataInitMessage dataInitMessage = JSONObject.parseObject(jsonStr, DataInitMessage.class);
            TenantContextHolder.setTenantId(dataInitMessage.getTenantId());
            ShopContextHolder.setShopId(dataInitMessage.getShopId());
            afsReasonService.init();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        channel.basicAck(m.getMessageProperties().getDeliveryTag(), false);
    }

    /**
     * 商家超时未审批自动通过申请
     *
     * @param message
     * @param properties
     * @param channel
     * @return void
     * @author alan
     * @date 2019/12/9 20:20
     */
    @RabbitListener(queues = AfsQueueNameConstant.AFS_MERCHANT_AUTO_CONFIRM)
    public void merchantAutoConfirmMessage(BaseAfsOrderMessage message, MessageProperties properties,
                                           Channel channel) throws IOException {
        log.info("收到商家超时未审批自动通过申请消息:deliveryTag{},当前时间{},消息内容{}.", properties.getDeliveryTag(), DateUtil.now(),
                message.toString());
        try {
            TenantContextHolder.setTenantId(message.getTenantId());
            ShopContextHolder.setShopId(message.getShopId());
            manageAfsOrderService.sellerApprove(message.getId(), true);
            log.info("商家超时未审批自动通过申请执行完成: deliveryTag{}", properties.getDeliveryTag());
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
        } finally {
            channel.basicAck(properties.getDeliveryTag(), true);
        }
    }


    /**
     * 用户确认退货超时
     *
     * @param message
     * @param properties
     * @param channel
     * @return void
     * @author alan
     * @date 2019/12/9 20:20
     */
    @RabbitListener(queues = AfsQueueNameConstant.AFS_USER_RETURN_OVERTIME)
    public void userReturnOvertimeMessage(BaseAfsOrderMessage message, MessageProperties properties, Channel channel) throws IOException {
        log.info("收到用户确认退货超时消息:deliveryTag{},当前时间{},消息内容{}.", properties.getDeliveryTag(), DateUtil.now(),
                message.toString());
        try {
            TenantContextHolder.setTenantId(message.getTenantId());
            ShopContextHolder.setShopId(message.getShopId());
            afsOrderService.userCancel(message.getId(), true);
            log.info("用户确认退货超时执行完成: deliveryTag{}", properties.getDeliveryTag());
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
        } finally {
            channel.basicAck(properties.getDeliveryTag(), true);
        }
    }


    @RabbitListener(queues = AfsQueueNameConstant.ORDER_RECEIPT)
    public void orderReceiptMessage(OrderVo orderVo, MessageProperties properties, Channel channel) throws IOException {
        log.info("换货单签收消息:" + orderVo.toString());
        try {
            //换货单签收之后更新相关的售后单状态
            TenantContextHolder.setTenantId(orderVo.getTenantId());
            ShopContextHolder.setShopId(orderVo.getShopId());
            afsOrderService.orderReceipt(orderVo);
            log.info("换货单签收执行完成: deliveryTag{}", properties.getDeliveryTag());
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
        } finally {
            channel.basicAck(properties.getDeliveryTag(), true);
        }
    }

    @RabbitListener(queues = AfsQueueNameConstant.ORDER_SHIPPED)
    public void orderShippedMessage(OrderVo orderVo, MessageProperties properties, Channel channel) throws IOException {
        log.info("换货单发货消息:" + orderVo.toString());
        try {
            TenantContextHolder.setTenantId(orderVo.getTenantId());
            ShopContextHolder.setShopId(orderVo.getShopId());
            afsOrderService.orderShipped(orderVo);
            log.info("换货单发货执行完成: deliveryTag{}", properties.getDeliveryTag());
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
        } finally {
            channel.basicAck(properties.getDeliveryTag(), true);
        }
    }

    @RabbitListener(queues = AfsQueueNameConstant.DELIVER_RECEIPT)
    public void deliverReceiptMessage(OrderVo orderVo, MessageProperties properties, Channel channel) throws IOException {
        log.info("发货单签收消息:" + orderVo.toString());
        try {
            TenantContextHolder.setTenantId(orderVo.getTenantId());
            ShopContextHolder.setShopId(orderVo.getShopId());
            afsOrderService.deliverReceipt(orderVo);
            log.info("换货单发货执行完成: deliveryTag{}", properties.getDeliveryTag());
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
        } finally {
            channel.basicAck(properties.getDeliveryTag(), true);
        }
    }

    @RabbitListener(queues = AfsQueueNameConstant.ORDER_COMPLETED)
    public void orderCompletedMessage(OrderVo orderVo, MessageProperties properties, Channel channel) throws IOException {
        log.info("用户评价消息:" + orderVo.toString());
        try {
            TenantContextHolder.setTenantId(orderVo.getTenantId());
            ShopContextHolder.setShopId(orderVo.getShopId());
            afsOrderService.orderCompleted(orderVo);
            log.info("用户评价消息执行完成: deliveryTag{}", properties.getDeliveryTag());
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
        } finally {
            channel.basicAck(properties.getDeliveryTag(), true);
        }
    }

}

