package com.medusa.gruul.afs.api.constant;

import java.math.BigDecimal;

/**
 * The interface Afs constant.
 *
 * @author alan
 * @description: 订单常用变量
 * @date 2019 /11/20 21:20
 */
public interface AfsConstant {

    /**
     * 最小付款额
     */
    BigDecimal MIN_PAY_FEE = BigDecimal.valueOf(0.01);
    /**
     * 售后模块默认交换机
     */
    String EXCHANGE_NAME = "gruul.afs.direct";
    /**
     * 售后延迟队列交换机
     */
    String DELAY_EXCHANGE_NAME = "gruul.afs.delay.direct";
    /**
     * 物流模块默认交换机
     */
    String DELIVER_EXCHANGE_NAME = "gruul.deliver.direct";

    String ACCOUNT_EXCHANGE = "gruul.account.exchange";
}
