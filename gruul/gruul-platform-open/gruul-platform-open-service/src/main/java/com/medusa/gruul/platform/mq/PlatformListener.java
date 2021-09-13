package com.medusa.gruul.platform.mq;

import com.alibaba.fastjson.JSON;
import com.medusa.gruul.platform.api.constant.QueueNameConstant;
import com.medusa.gruul.platform.api.model.dto.SubscribeMsgSendDto;
import com.medusa.gruul.platform.service.IPlatformShopInfoService;
import com.medusa.gruul.platform.service.IPlatformShopMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author whh
 * @description 平台监听队列
 * @data: 2020-02-02
 */
@Slf4j
@Component
public class PlatformListener {

    @Autowired
    private IPlatformShopMessageService platformShopMessageService;
    @Autowired
    private IPlatformShopInfoService platformShopInfoService;

    /**
     * 订阅消息发送队列(只负责接收发送,不完全保证发送成功,只发送一次无论失败成功)
     *
     * @param msgSendDto com.medusa.gruul.platform.api.model.dto.SubscribeMsgSendDto
     */
    @RabbitListener(queues = QueueNameConstant.PLATFORM_SUBSCRIBE_MSG_SEND)
    public void subscribeMsgSendReceive(SubscribeMsgSendDto msgSendDto) {
        log.debug("receive message:" + JSON.toJSONString(msgSendDto));
        try {
            platformShopMessageService.subscribeMsgSend(msgSendDto);
        } catch (Exception e) {
            log.debug("订阅消息发送队列错误 : {}  ", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 套餐延迟队列
     *
     * @param platefromInfoId java.lang.Integer
     */
    @RabbitListener(queues = QueueNameConstant.PLATFORM_PACKAGE_DUE)
    public void packageDueReceive(Integer platefromInfoId) {
        log.debug("receive message:" + platefromInfoId);
        try {
            platformShopInfoService.packageDueReceive(platefromInfoId);
        } catch (Exception e) {
            log.debug("套餐延迟队列错误 : {}  ", e.getMessage());
            e.printStackTrace();
        }

    }
}
