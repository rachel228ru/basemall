package com.medusa.gruul.order.mq;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.medusa.gruul.order.api.constant.OrderQueueEnum;
import com.medusa.gruul.order.api.constant.OrderQueueNameConstant;
import com.medusa.gruul.order.api.entity.OrderItem;
import com.medusa.gruul.order.api.model.*;
import com.medusa.gruul.platform.api.constant.ExchangeConstant;
import com.medusa.gruul.platform.api.constant.QueueNameConstant;
import com.medusa.gruul.platform.api.model.dto.SubscribeMsgSendDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedList;

/**
 * Sender.java
 *
 * @author alan
 * @date 2019/10/6 13:48
 */
@Slf4j
@Component
public class Sender {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**
     * 订单创建
     *
     * @param orderMessage
     * @return void
     * @author alan
     * @date 2019/12/9 20:17
     */
    public void sendCreateOrderMessage(CreateOrderMessage orderMessage) {
        log.info("sendCreateOrderMessage:" + orderMessage.toString());
        convertAndSend(OrderQueueEnum.QUEUE_ORDER_CREATE, orderMessage);
    }

    /**
     *
     *
     * @param orderMessage
     * @return void
     * @author alan
     * @date 2019/12/9 20:17
     */
    public void sendCreateOrderFailMessage(OrderFailMessage orderMessage) {
        log.info("sendCreateOrderFailMessage:" + orderMessage.toString());
        convertAndSend(OrderQueueEnum.QUEUE_ORDER_CREATE_FAIL, orderMessage);
    }

    /**
     * 订单超时未支付自动取消
     *
     * @param message
     * @param expiration
     * @return void
     * @author alan
     * @date 2019/12/9 20:17
     */
    public void sendAutoCancelOrderMessage(BaseOrderMessage message, long expiration) {
        log.info("sendAutoCancelOrderMessage:" + message);
        log.info("send time:" + LocalDateTime.now());
        convertAndSend(OrderQueueEnum.QUEUE_ORDER_AUTO_CANCEL, message, new ExpirationMessagePostProcessor(expiration));
    }

    /**
     * 订单取消失败
     *
     * @param message
     * @return void
     * @author alan
     * @date 2019/12/9 20:17
     */
    public void sendCancelFailOrderMessage(OrderFailMessage message) {
        log.info("sendCancelFailOrderMessage:" + message);
        log.info("send time:" + LocalDateTime.now());
        convertAndSend(OrderQueueEnum.QUEUE_ORDER_CANCEL_FAIL, message);
    }


    /**
     * 订单超时未收货自动确认
     *
     * @param message
     * @param expiration
     * @return void
     * @author alan
     * @date 2019/12/9 20:17
     */
    public void sendAutoReceiptOrderMessage(BaseOrderMessage message, long expiration) {
        log.info("sendAutoReceiptOrderMessage:{},time:{}", message.toString(), expiration);
        convertAndSend(OrderQueueEnum.QUEUE_ORDER_AUTO_RECEIPT, message,
                new ExpirationMessagePostProcessor(expiration));
    }


    /**
     * 订单超时未评价自动完成
     *
     * @param message
     * @param expiration
     * @return void
     * @author alan
     * @date 2019/12/9 20:17
     */
    public void sendAutoCompletedOrderMessage(BaseOrderMessage message, long expiration) {
        log.info("sendAutoCompletedOrderMessage:" + message.toString());
        convertAndSend(OrderQueueEnum.QUEUE_ORDER_AUTO_COMPLETED, message,
                new ExpirationMessagePostProcessor(expiration));
    }

    /**
     * 订单发货发送微信订阅消息
     *
     * @return void
     * @author alan
     * @date 2019/12/9 20:17
     */
    public void sendDeliveryMessage(OrderVo orderVo, String openId) {
        log.info("sendDeliveryMessage start:" + orderVo.getId());

        SubscribeMsgSendDto dto = new SubscribeMsgSendDto();
        dto.setTemplateId(orderVo.getOrderDelivery().getDeliveryTemplateId());
        dto.setOpenId(openId);
        dto.setToPath(StrUtil.format("/pages/orderDetail/orderDetail?orderId={}", orderVo.getId()));
        dto.setTenantId(orderVo.getTenantId());
        LinkedList<String> s = new LinkedList<>();
        //订单编号：20191111232312
        //发货时间：2019年12月12日 14:02
        //商品详情：华为手机1件
        //快递单号：sf2312312122
        //收货人：某某某
        s.add(orderVo.getOrderDelivery().getOrderId().toString());
        s.add(DateTime.now().toString("yyyy年MM月dd日 HH:mm"));
        String itemDesc = getItemDesc(orderVo);
        s.add(itemDesc);
        String deliverySn = orderVo.getOrderDelivery().getDeliverySn();
        if (StrUtil.isBlank(deliverySn) || "无".equals(deliverySn)) {
            deliverySn = "-";
        }
        s.add(deliverySn);
        String receiverName = orderVo.getOrderDelivery().getReceiverName();
        if (StrUtil.isBlank(deliverySn)) {
            receiverName = "-";
        }
        s.add(receiverName);
        dto.setSendDatas(s);
        CorrelationData correlationData = new CorrelationData(IdUtil.fastSimpleUUID());

        rabbitTemplate.convertAndSend(ExchangeConstant.PLATFORM_EXCHANGE,
                QueueNameConstant.PLATFORM_SUBSCRIBE_MSG_SEND, dto, correlationData);
        log.info("sendDeliveryMessage end:" + orderVo.getId());

    }

    private String getItemDesc(OrderVo orderVo) {
        String productName = StrUtil.subPre(CollUtil.getFirst(orderVo.getOrderItemList()).getProductName(), 12);
        Integer number = orderVo.getOrderItemList().stream().mapToInt(OrderItem::getProductQuantity).sum();
        return productName.concat("等商品").concat(number.toString()).concat("件");
    }


    /**
     * 订单移出发货单
     *
     * @param message
     * @return void
     * @author alan
     * @date 2019/12/9 20:18
     */
    public void sendRemoveSendBillOrderMessage(RemoveSendBillOrderMessage message) {
        CorrelationData correlationData = new CorrelationData(IdUtil.fastSimpleUUID());
        log.info("sendRemoveSendBillOrderMessage:" + message.toString());
        rabbitTemplate.convertAndSend(OrderQueueNameConstant.DELIVER_REMOVE, message, correlationData);
    }

    public void generateSendBillOrderMessage(GenerateSendBillOrderMessage message) {
        CorrelationData correlationData = new CorrelationData(IdUtil.fastSimpleUUID());
        log.info("generateSendBillOrderMessage message:" + message.toString());
        rabbitTemplate.convertAndSend(OrderQueueEnum.QUEUE_DELIVER_CREATE.getExchange(),
                OrderQueueEnum.QUEUE_DELIVER_CREATE.getRouteKey(), message, correlationData);

    }

    public void sendReturnOrderMessage(OrderVo orderVo) {
        CorrelationData correlationData = new CorrelationData(IdUtil.fastSimpleUUID());
        log.info("sendReturnOrderMessage message:" + orderVo.toString());
        rabbitTemplate.convertAndSend(OrderQueueEnum.QUEUE_ORDER_RETURN.getExchange(),
                OrderQueueEnum.QUEUE_ORDER_RETURN.getRouteKey(), orderVo, correlationData);

    }

    public void receiptSendBillOrderMessage(OrderVo orderVo) {
        CorrelationData correlationData = new CorrelationData(IdUtil.fastSimpleUUID());
        log.info("receiptSendBillOrderMessage message:" + orderVo.toString());
        rabbitTemplate.convertAndSend(OrderQueueEnum.QUEUE_DELIVER_RECEIPT.getExchange(),
                OrderQueueEnum.QUEUE_DELIVER_RECEIPT.getRouteKey(), orderVo, correlationData);

    }


    //============================================订阅==============================================


    /**
     * 订单支付成功订阅
     *
     * @param orderVo
     * @return void
     * @author alan
     * @date 2019/12/9 20:17
     */
    public void sendPayedOrderMessage(OrderVo orderVo) {
        log.info("sendPayedOrderMessage:" + orderVo.toString());
        orderVo.setProductTotalQuantity(orderVo.getOrderItemList().stream().mapToInt(OrderItem::getProductQuantity).sum());
        convertAndSend(OrderQueueEnum.QUEUE_ORDER_PAYED, orderVo);
    }

    /**
     * 订单关闭成功订阅
     *
     * @param orderVo
     * @return void
     * @author alan
     * @date 2019/12/9 20:17
     */
    public void sendCloseOrderMessage(OrderVo orderVo) {
        log.info("sendCloseOrderMessage:" + orderVo.toString());
        convertAndSend(OrderQueueEnum.QUEUE_ORDER_CLOSE, orderVo);
    }

    /**
     * 订单发货订阅
     *
     * @param orderVo
     * @return void
     * @author alan
     * @date 2019/12/9 20:17
     */
    public void sendShippedOrderMessage(OrderVo orderVo) {
        log.info("sendShippedOrderMessage:" + orderVo.toString());
        convertAndSend(OrderQueueEnum.QUEUE_ORDER_SHIPPED, orderVo);
    }

    /**
     * 订单签收成功订阅
     *
     * @param orderVo
     * @return void
     * @author alan
     * @date 2019/12/9 20:17
     */
    public void sendReceiptOrderMessage(OrderVo orderVo) {
        log.info("sendReceiptOrderMessage:" + orderVo.toString());
        orderVo.setProductTotalQuantity(orderVo.getOrderItemList().stream().mapToInt(OrderItem::getProductQuantity).sum());
        convertAndSend(OrderQueueEnum.QUEUE_ORDER_RECEIPT, orderVo);
    }

    /**
     * 订单完成订阅
     *
     * @param orderVo
     * @return void
     * @author alan
     * @date 2019/12/9 20:18
     */
    public void sendCompletedOrderMessage(OrderVo orderVo) {
        log.info("sendCompletedOrderMessage:" + orderVo.toString());
        orderVo.setProductTotalQuantity(orderVo.getOrderItemList().stream().mapToInt(OrderItem::getProductQuantity).sum());
        convertAndSend(OrderQueueEnum.QUEUE_ORDER_COMPLETED, orderVo);
    }

    private void convertAndSend(OrderQueueEnum queue, Object message) {
        CorrelationData correlationData = new CorrelationData(IdUtil.fastSimpleUUID());
        rabbitTemplate.convertAndSend(queue.getExchange(), queue.getRouteKey(), message, correlationData);
    }

    private void convertAndSend(OrderQueueEnum queue, Object message, MessagePostProcessor messagePostProcessor) {
        CorrelationData correlationData = new CorrelationData(IdUtil.fastSimpleUUID());
        rabbitTemplate.convertAndSend(queue.getExchange(), queue.getRouteKey(), message, messagePostProcessor,
                correlationData);
    }

    /**
     * 订单退款成功订阅
     *
     * @param vo  订单信息
     * @author xiaoq
     *
     */
    public void sendRefundSuccess(OrderVo vo) {
        log.info("sendRefundSuccess:" + vo.toString());
        vo.setProductTotalQuantity(vo.getOrderItemList().stream().mapToInt(OrderItem::getProductQuantity).sum());
        convertAndSend(OrderQueueEnum.QUEUE_ORDER_RETURN_SUCCEED, vo);
    }
}