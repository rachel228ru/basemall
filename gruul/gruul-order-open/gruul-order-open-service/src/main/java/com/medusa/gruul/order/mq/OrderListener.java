package com.medusa.gruul.order.mq;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.medusa.gruul.common.core.constant.TimeConstants;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.common.dto.CurUserDto;
import com.medusa.gruul.goods.api.constant.GoodsSkuStockRedisKey;
import com.medusa.gruul.goods.api.entity.SkuStock;
import com.medusa.gruul.goods.api.feign.RemoteGoodsService;
import com.medusa.gruul.order.api.constant.OrderFailedRedisKey;
import com.medusa.gruul.order.api.constant.OrderQueueNameConstant;
import com.medusa.gruul.order.api.model.*;
import com.medusa.gruul.order.service.IMiniOrderService;
import com.medusa.gruul.order.service.IOrderDeliveryService;
import com.medusa.gruul.order.service.IOrderSettingService;
import com.medusa.gruul.order.service.IOrderShareSettingService;
import com.medusa.gruul.payment.api.model.dto.RefundNotifyResultDto;
import com.medusa.gruul.payment.api.model.dto.WxPayNotifyResultDto;
import com.medusa.gruul.platform.api.constant.ExchangeConstant;
import com.medusa.gruul.platform.api.constant.QueueNameConstant;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Receiver.java
 *
 * @author alan
 * @date 2019/10/6 13:49
 */
@Slf4j
@Component
public class OrderListener {
    @Resource
    private IMiniOrderService orderService;
    @Resource
    private IOrderSettingService orderSettingService;
    @Resource
    private IOrderShareSettingService orderShareSettingService;
    @Resource
    private IOrderDeliveryService orderDeliveryService;
    @Resource
    private RemoteGoodsService remoteGoodsService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

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
            orderSettingService.init();
            orderShareSettingService.init();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        channel.basicAck(m.getMessageProperties().getDeliveryTag(), false);
    }

    /**
     * 订单创建消费者
     *
     * @param orderMessage
     * @param channel
     * @param message
     * @return void
     * @author alan
     * @date 2019/12/9 20:20
     */
    @RabbitListener(queues = OrderQueueNameConstant.ORDER_CREATE)
    public void createOrder(CreateOrderMessage orderMessage, Channel channel, Message message) throws IOException {
        log.info("收到订单创建消息:deliveryTag{},当前时间{},消息内容{}.", message.getMessageProperties().getDeliveryTag(),
                DateUtil.now(),
                orderMessage.toString());
        try {
            CreateOrderDto createOrderDto = orderMessage.getOrderVo();
            Long orderId = orderMessage.getOrderId();
            CurUserDto curUser = orderMessage.getCurUser();
            TenantContextHolder.setTenantId(orderMessage.getTenantId());
            ShopContextHolder.setShopId(orderMessage.getCurUser().getShopId());
            List<ItemDto> itemDtoList = createOrderDto.getItemDtoList();
            List<SkuStock> skuStockList = new ArrayList<>(itemDtoList.size());
            OrderFailedRedisKey orderFailed = new OrderFailedRedisKey();
            log.info("sku is {}", itemDtoList.size());
            for (ItemDto itemDto : itemDtoList) {
                //判断库存
                log.info("search skuStock {} start", itemDto.getSkuId());
                SkuStock skuStock = remoteGoodsService.findSkuStockById(itemDto.getSkuId());
                if (ObjectUtil.isNull(skuStock)) {
                    orderFailed.setNxPx(orderId.toString(), "商品信息已经更新", TimeConstants.ONE_DAY);
                    throw new ServiceException("商品信息已经更新");
                }
                log.info("skuStock is {}", skuStock.toString());

                int stock = skuStock.getStock();
                if (stock <= 0) {
                    new GoodsSkuStockRedisKey().set(itemDto.getSkuId().toString(), stock + "");
                    orderFailed.setNxPx(orderId.toString(), "商品已售罄", TimeConstants.ONE_DAY);
                    throw new ServiceException("商品已售罄");
                }
                skuStockList.add(skuStock);

            }
            //减库存 下订单 写入订单
            Boolean succ = orderService.createOrder(createOrderDto, orderId, skuStockList, curUser);
            if (succ) {
                log.info("订单创建执行成功: deliveryTag{}", message.getMessageProperties().getDeliveryTag());
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
            } else {
                throw new ServiceException("订单创建失败");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.info("订单创建执行失败: deliveryTag{}", message.getMessageProperties().getDeliveryTag());

            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
        }
    }


    /**
     * 超时自动取消
     *
     * @param message
     * @param properties
     * @param channel
     * @return void
     * @author alan
     * @date 2019/12/9 20:20
     */
    @RabbitListener(queues = OrderQueueNameConstant.ORDER_AUTO_CANCEL)
    public void autoCancelOrder(BaseOrderMessage message, MessageProperties properties, Channel channel) throws IOException {
        log.info("收到超时自动取消消息:deliveryTag{},当前时间{},消息内容{}.", properties.getDeliveryTag(), DateUtil.now(),
                message.toString());

        try {
            TenantContextHolder.setTenantId(message.getTenantId());
            ShopContextHolder.setShopId(message.getShopId());
            orderService.autoCancelOrder(message.getOrderId());
            log.info("超时自动取消执行完成: deliveryTag{}", properties.getDeliveryTag());
            channel.basicAck(properties.getDeliveryTag(), true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


    /**
     * 用户超时未确认收货自动签收
     *
     * @param message
     * @param properties
     * @param channel
     * @return void
     * @author alan
     * @date 2019/12/9 20:21
     */
    @RabbitListener(queues = OrderQueueNameConstant.ORDER_AUTO_RECEIPT)
    public void autoReceiptOrder(BaseOrderMessage message, MessageProperties properties, Channel channel) throws IOException {
        log.info("收到自动签收消息:deliveryTag{},当前时间{},消息内容{}.", properties.getDeliveryTag(), DateUtil.now(),
                message.toString());
        try {
            TenantContextHolder.setTenantId(message.getTenantId());
            ShopContextHolder.setShopId(message.getShopId());
            orderService.receiptOrder(message.getOrderId(), true);
            log.info("超时自动完成执行完成: deliveryTag{}", properties.getDeliveryTag());
            channel.basicAck(properties.getDeliveryTag(), true);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 超时自动完成
     *
     * @param message
     * @param properties
     * @param channel
     * @return void
     * @author alan
     * @date 2019/12/9 20:20
     */
    @RabbitListener(queues = OrderQueueNameConstant.ORDER_AUTO_COMPLETED)
    public void autoCompleteOrder(BaseOrderMessage message, MessageProperties properties, Channel channel) throws IOException {
        log.info("收到超时自动完成消息:deliveryTag{},当前时间{},消息内容{}.", properties.getDeliveryTag(), DateUtil.now(),
                message.toString());
        try {
            TenantContextHolder.setTenantId(message.getTenantId());
            ShopContextHolder.setShopId(message.getShopId());
            orderService.evaluateOrder(message.getOrderId());
            log.info("超时自动完成执行完成: deliveryTag{}", properties.getDeliveryTag());
            channel.basicAck(properties.getDeliveryTag(), true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


    /**
     * 订单支付回调
     *
     * @param message
     * @param properties
     * @param channel
     * @return void
     * @author alan
     * @date 2020/1/8 21:02
     */
    @RabbitListener(queues = OrderQueueNameConstant.PAYMENT_NOTIFY)
    public void paymentNotify(WxPayNotifyResultDto message, MessageProperties properties, Channel channel) throws IOException {
        log.info("收到支付回调消息:deliveryTag{},当前时间{},消息内容{}.", properties.getDeliveryTag(), DateUtil.now(),
                message.toString());
        try {
            orderService.paymentNotify(Long.parseLong(message.getOutTradeNo()), message.getTenantId());
            log.info("支付回调执行完成: deliveryTag{}", properties.getDeliveryTag());
            channel.basicAck(properties.getDeliveryTag(), true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 订单退款回调
     *
     * @param message
     * @param properties
     * @param channel
     * @return void
     * @author alan
     * @date 2020/1/8 21:02
     */
    @RabbitListener(queues = OrderQueueNameConstant.REFUND_NOTIFY)
    public void refundNotify(RefundNotifyResultDto message, MessageProperties properties, Channel channel) throws IOException {
        log.info("订单退款回调:deliveryTag{},当前时间{},消息内容{}.", properties.getDeliveryTag(), DateUtil.now(),
                message.toString());
        try {
            orderService.refundNotify(message);
            log.info("支付回调执行完成: deliveryTag{}", properties.getDeliveryTag());
            channel.basicAck(properties.getDeliveryTag(), true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


    /**
     * 物流模块的发货单创建及发货
     *
     * @param message
     * @param properties
     * @param channel
     * @return void
     * @author alan
     * @date 2019/12/9 20:20
     */
    @RabbitListener(queues = OrderQueueNameConstant.DELIVER_CREATE)
    public void deliverCreate(GenerateSendBillOrderMessage message, MessageProperties properties, Channel channel) throws IOException {
        log.info("收到发货单创建消息:当前时间{},消息内容{}.", DateUtil.now(), message.toString());
        try {
            TenantContextHolder.setTenantId(message.getTenantId());
            if (message.getOrderIds().isEmpty()) {
                log.info("发货单创建执行失败,失败原因{}, 当前时间{}", "订单ID不得为空", DateUtil.now());
            } else {
                orderDeliveryService.updateShipping(message);
                log.info("发货单创建执行完成: 当前时间{}", DateUtil.now());
            }
            channel.basicAck(properties.getDeliveryTag(), true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


    /**
     * 物流模块发回的签收单签收
     *
     * @param message
     * @param properties
     * @param channel
     * @return void
     * @author alan
     * @date 2019/12/9 20:21
     */
    @RabbitListener(queues = OrderQueueNameConstant.DELIVER_RECEIPT)
    public void deliverReceipt(ReceiptSendBillOrderMessage message, MessageProperties properties, Channel channel) throws IOException {
        log.info("收到签收单签收消息:deliveryTag{},当前时间{},消息内容{}.", properties.getDeliveryTag(), DateUtil.now(),
                message.toString());
        try {
            TenantContextHolder.setTenantId(message.getTenantId());
            if (message.getOrderIds().isEmpty()) {
                log.info("发货单创建执行失败,失败原因{}, 当前时间{}", "订单ID不得为空", DateUtil.now());
            } else {
                orderDeliveryService.receiptSendBill(message);
                log.info("签收单签收执行完成: deliveryTag{}", properties.getDeliveryTag());
            }
            channel.basicAck(properties.getDeliveryTag(), true);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}

