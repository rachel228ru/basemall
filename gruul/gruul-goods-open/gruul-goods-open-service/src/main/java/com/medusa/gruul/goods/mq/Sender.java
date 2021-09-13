package com.medusa.gruul.goods.mq;

import cn.hutool.core.date.DateUtil;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.goods.api.constant.ShoppingCartMyQueneName;
import com.medusa.gruul.goods.api.entity.ShoppingCart;
import com.medusa.gruul.goods.api.model.dto.manager.SupplierDto;
import com.medusa.gruul.platform.api.constant.ExchangeConstant;
import com.medusa.gruul.platform.api.constant.QueueNameConstant;
import com.medusa.gruul.platform.api.model.dto.SubscribeMsgSendDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

/**
 * <p>
 * mq发送
 * </p>
 *
 * @author lcysike
 * @since 2019-11-19
 */
@Slf4j
@Component
public class Sender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    public void sendShoppingCartMessage(ShoppingCartMessage shoppingCartMessage) {
        ShoppingCart shoppingCart = new ShoppingCart();
        log.info(shoppingCart.toString());
        log.info("send message:" + shoppingCartMessage.toString());
        amqpTemplate.convertAndSend(ShoppingCartMyQueneName.GENERATE_SHOPPING_CART, shoppingCartMessage);
    }

    /**
     * 供应商审核发送微信订阅消息
     *
     * @param openId
     * @param supplierDto
     */
    public void sendSupplierMessage(SupplierDto supplierDto, String openId, String tenantId, String templateId) {
        log.info("-----------供应商审核订阅消息发送开始-----------");
        SubscribeMsgSendDto dto = new SubscribeMsgSendDto();
        dto.setTemplateId(templateId);
        dto.setOpenId(openId);
        dto.setToPath("pages/index/index");
        dto.setTenantId(tenantId);
        LinkedList<String> s = new LinkedList<>();
        if(CommonConstants.NUMBER_ONE.equals(supplierDto.getStatus())){
            s.add("您已通过供应商审核，请联系商家！");
            s.add(DateUtil.now());
            s.add(supplierDto.getName());
        }else{
            s.add("申请成为供应商被拒绝，请联系商家！");
            s.add(DateUtil.now());
            s.add(supplierDto.getName());
        }
        dto.setSendDatas(s);
        rabbitTemplate.convertAndSend(ExchangeConstant.PLATFORM_EXCHANGE, QueueNameConstant.PLATFORM_SUBSCRIBE_MSG_SEND,dto);
        log.info("-----------供应商审核订阅消息发送结束-----------");

    }
}
