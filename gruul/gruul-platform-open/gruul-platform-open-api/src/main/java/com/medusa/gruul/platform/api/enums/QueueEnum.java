package com.medusa.gruul.platform.api.enums;

import com.medusa.gruul.platform.api.constant.ExchangeConstant;
import com.medusa.gruul.platform.api.constant.QueueNameConstant;
import lombok.Getter;

/**
 * @author whh
 */
@Getter
public enum QueueEnum {

    /**
     * 订阅消息发送队列
     */
    PLATFORM_SUBSCRIBE_MSG_SEND(ExchangeConstant.PLATFORM_EXCHANGE, QueueNameConstant.PLATFORM_SUBSCRIBE_MSG_SEND, QueueNameConstant.PLATFORM_SUBSCRIBE_MSG_SEND),
    /**
     * 套餐延迟队列
     */
    PLATFORM_PACKAGE_DUE(ExchangeConstant.PLATFORM_DELAY_EXCHANGE, QueueNameConstant.PLATFORM_PACKAGE_DUE, QueueNameConstant.PLATFORM_PACKAGE_DUE),
    ;

    /**
     * 交换名称
     */
    private String exchange;
    /**
     * 队列名称
     */
    private String name;
    /**
     * 路由键
     */
    private String routeKey;

    QueueEnum(String exchange, String name, String routeKey) {
        this.exchange = exchange;
        this.name = name;
        this.routeKey = routeKey;
    }
}
