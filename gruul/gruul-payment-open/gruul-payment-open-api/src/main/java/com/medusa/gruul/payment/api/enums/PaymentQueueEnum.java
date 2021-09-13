package com.medusa.gruul.payment.api.enums;

import com.medusa.gruul.payment.api.constant.PaymentExchangeConstant;
import com.medusa.gruul.payment.api.constant.PaymentQueueNameConstant;
import lombok.Getter;

/**
 * @author whh
 */
@Getter
public enum PaymentQueueEnum {

    /**
     * 小程序支付成功通知
     */
    QUEUE_PAYMENT_NOTIFY_SUCCESS(PaymentExchangeConstant.PAYMENT_EXCHANGE, PaymentQueueNameConstant.PAYMENT_MINI_OK_NOTIFY, "gruul.payment.mini.pay.notify.success"),
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

    PaymentQueueEnum(String exchange, String name, String routeKey) {
        this.exchange = exchange;
        this.name = name;
        this.routeKey = routeKey;
    }
}
