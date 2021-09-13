package com.medusa.gruul.account.api.enums;

import com.medusa.gruul.account.api.constant.AccountExchangeConstant;
import com.medusa.gruul.account.api.constant.AccountQueueNameConstant;
import lombok.Getter;

/**
 * @author whh
 */
@Getter
public enum AccountQueueEnum {

    /**
     * 积分对应的加减队列
     */
    QUEUE_ACCOUNT_INTEGRAL_CHANGE(AccountExchangeConstant.ACCOUNT_EXCHANGE, AccountQueueNameConstant.ACCOUNT_INTEGRAL_QUEUE_CHANGE, "gruul.account.integral.queue.change"),

    /**
     * 用户收藏更改数据库队列
     */
    QUEUE_ACCOUNT_COLLECT(AccountExchangeConstant.ACCOUNT_EXCHANGE, AccountQueueNameConstant.ACCOUNT_COLLECT, "gruul.account.collect"),


    /**
     * 默认用户队列
     */
    QUEUE_ACCOUNT_DEFAULT(AccountExchangeConstant.ACCOUNT_EXCHANGE, AccountQueueNameConstant.ACCOUNT_DEFAULT, AccountQueueNameConstant.ACCOUNT_DEFAULT),

    /**
     * 会员支付队列
     */
    QUEUE_MEMBER_PAY(AccountExchangeConstant.MEMBER_EXCHANGE, AccountQueueNameConstant.MEMBER_PAY_OK_QUEUE_CHANGE, AccountQueueNameConstant.MEMBER_PAY_OK_QUEUE_CHANGE);


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

    AccountQueueEnum(String exchange, String name, String routeKey) {
        this.exchange = exchange;
        this.name = name;
        this.routeKey = routeKey;
    }
}
