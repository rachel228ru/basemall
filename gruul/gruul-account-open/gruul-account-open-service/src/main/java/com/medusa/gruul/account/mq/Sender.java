package com.medusa.gruul.account.mq;

import com.medusa.gruul.account.api.constant.AccountQueueNameConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author xiaoq
 *
 * @data 2020/2/24 13:35
 */
@Slf4j
@Component
public class Sender {
    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendShoppingCartMessage(CollectMessage collectMessage) {
        log.info("send message:" + collectMessage.toString());
        amqpTemplate.convertAndSend(AccountQueueNameConstant.ACCOUNT_COLLECT, collectMessage);
    }
}
