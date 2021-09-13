package com.medusa.gruul.afs.mp;


import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.medusa.gruul.afs.api.constant.AfsQueueEnum;
import com.medusa.gruul.afs.api.enums.AfsOrderStatusEnum;
import com.medusa.gruul.afs.model.AfsOrderVo;
import com.medusa.gruul.afs.mp.model.AfsRemoveDeliverOrderMessage;
import com.medusa.gruul.afs.mp.model.BaseAfsOrderMessage;
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
 * @author alan
 * @Description: Sender.java
 * @date 2019/10/6 13:48
 */
@Slf4j
@Component
public class Sender {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**
     * 商家超时未审批自动通过申请
     *
     * @param message
     * @param expiration
     * @return void
     * @author alan
     * @date 2019/12/9 20:17
     */
    public void sendMerchantAutoConfirmMessage(BaseAfsOrderMessage message, long expiration) {
        log.info("sendMerchantAutoConfirmMessage:" + message);
        log.info("send time:" + LocalDateTime.now());
        convertAndSend(AfsQueueEnum.QUEUE_AFS_MERCHANT_AUTO_CONFIRM, message,
                new ExpirationMessagePostProcessor(expiration));
    }

    /**
     * 订单移出发货单
     *
     * @param message
     * @return void
     * @author alan
     * @date 2019/12/9 20:18
     */
    public void sendRemoveSendBillOrderMessage(AfsRemoveDeliverOrderMessage message) {
        CorrelationData correlationData = new CorrelationData(IdUtil.fastSimpleUUID());
        log.info("sendRemoveSendBillOrderMessage:" + message.toString());
        rabbitTemplate.convertAndSend("gruul.afs.deliver.remove", message, correlationData);
    }


    private void convertAndSend(AfsQueueEnum queue, Object message, MessagePostProcessor messagePostProcessor) {
        CorrelationData correlationData = new CorrelationData(IdUtil.fastSimpleUUID());
        rabbitTemplate.convertAndSend(queue.getExchange(), queue.getRouteKey(), message, messagePostProcessor,
                correlationData);
    }

    /**
     * 发送退货消息通知
     *
     * @return void
     * @author alan
     * @date 2019/12/9 20:17
     */
    public void sendWechatReturnMessage(AfsOrderVo afsOrder, String openId) {
        log.info("sendWechatReturnMessage start:" + JSONUtil.toJsonStr(afsOrder));

        SubscribeMsgSendDto dto = new SubscribeMsgSendDto();
        dto.setTemplateId(afsOrder.getReturnTemplateId());
        dto.setOpenId(openId);
        dto.setToPath(StrUtil.format("/pages/afterSaleDetail/afterSaleDetail?afsid={}", afsOrder.getId()));
        dto.setTenantId(afsOrder.getTenantId());
        LinkedList<String> s = new LinkedList<>();
//        订单编号：1234567890
//        商品名称：iPhone
//        退款金额：100积分+￥2
//        退回积分：100积分
//        温馨提示：商家已同意退款申请，请及时退货！
        s.add(afsOrder.getItem().getOrderId().toString());
        if(afsOrder.getItem().getProductName().length() >= 20){
            s.add(afsOrder.getItem().getProductName().indexOf(0,16)+"...");
        }else{
            s.add(afsOrder.getItem().getProductName());
        }
        s.add("￥" + afsOrder.getRefundAmount().toString());
        // TODO: 2021/8/18 售后推送有积分要不要删除
        s.add(0 + "积分");
        s.add("商家已同意退款申请，请及时退货！");
        dto.setSendDatas(s);
        CorrelationData correlationData = new CorrelationData(IdUtil.fastSimpleUUID());

        rabbitTemplate.convertAndSend(ExchangeConstant.PLATFORM_EXCHANGE,
                QueueNameConstant.PLATFORM_SUBSCRIBE_MSG_SEND, dto, correlationData);
        log.info("sendWechatReturnMessage end:" + afsOrder.getId());
    }

    /**
     * 发送退款消息通知
     *
     * @return void
     * @author alan
     * @date 2019/12/9 20:17
     */
    public void sendWechatRefundMessage(AfsOrderVo afsOrder, String openId) {
        log.info("sendRefundMessage start:" + afsOrder.toString());
        SubscribeMsgSendDto dto = new SubscribeMsgSendDto();
        dto.setTemplateId(afsOrder.getTemplateId());
        dto.setOpenId(openId);
        dto.setToPath(StrUtil.format("/pages/afterSaleDetail/afterSaleDetail?afsid={}", afsOrder.getId()));
        dto.setTenantId(afsOrder.getTenantId());
        LinkedList<String> s = new LinkedList<>();
//        订单号：2019111900000001
//        商品名称：这是个商品名称
//        退款状态：退款失败
//        退款金额：￥100
//        退回积分：100积分
        s.add(afsOrder.getItem().getOrderId().toString());
        if(afsOrder.getItem().getProductName().length() >= 20){
            s.add(afsOrder.getItem().getProductName().indexOf(0,16)+"...");
        }else{
            s.add(afsOrder.getItem().getProductName());
        }
        if (afsOrder.getStatus().equals(AfsOrderStatusEnum.SUCCESS)) {
            s.add("退款成功");
        } else {
            s.add("退款失败");
        }
        s.add("￥" + afsOrder.getRefundAmount().toString());
        // TODO: 2021/8/18 售后推送
        s.add(0+ "积分");
        dto.setSendDatas(s);
        CorrelationData correlationData = new CorrelationData(IdUtil.fastSimpleUUID());

        rabbitTemplate.convertAndSend(ExchangeConstant.PLATFORM_EXCHANGE,
                QueueNameConstant.PLATFORM_SUBSCRIBE_MSG_SEND, dto, correlationData);
        log.info("sendRefundMessage end:" + afsOrder.getId());
    }

}