package com.medusa.gruul.order.api.constant;

import java.math.BigDecimal;

/**
 * 订单常用变量
 *
 * @author alan
 * @date 2019/11/20 21:20
 */
public interface OrderConstant {

    /**
     * 最小付款额
     */
    BigDecimal MIN_PAY_FEE = BigDecimal.valueOf(0.01);
    /**
     * 订单模块默认交换机
     */
    String EXCHANGE_NAME = "gruul.order.direct";
    /**
     * 订单延迟队列交换机
     */
    String DELAY_EXCHANGE_NAME = "gruul.order.delay.direct";
    /**
     * 物流模块默认交换机
     */
    String DELIVER_EXCHANGE_NAME = "gruul.deliver.direct";
    /**
     * 支付模块默认交换机
     */
    String PAYMENT_EXCHANGE = "payment.notify.exchange";
    /**
     * 账户模块默认交换机
     */
    String ACCOUNT_EXCHANGE = "gruul.account.exchange";

}
