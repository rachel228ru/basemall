package com.medusa.gruul.order.api.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 订单事件枚举
 *
 * @author alan
 * @date 2019/10/1 22:02
 */
@Getter
public enum OrderEvents {

    /**
     * 买家付款
     */
    BUYER_PAYMENT(100, "买家付款"),
    /**
     * 卖家发货
     */
    SELLER_SEND(101, "卖家发货"),
    /**
     * 商品到达提货点
     */
    ARRIVED(102, "商品到达提货点"),
    /**
     * 买家签收
     */
    BUYER_SIGNED(103, "用户签收"),
    /**
     * 买家评价
     */
    BUYER_COMMENT(104, "用户评价"),
    /**
     * 订单完成
     */
    COMPLETED(105, "订单完成"),
    /**
     * 退货成功
     */
    RETURN_SUCCESS(200, "退货成功"),
    /**
     * 退款成功
     */
    REFUNDED_SUCCESS(201, "退款成功"),
    /**
     * 部分退款成功
     */
    PARTIAL_REFUNDED_SUCCESS(202, "部分退款成功"),
    /**
     * 支付超时关闭
     */
    BUYER_PAY_TIMEOUT(300, "支付超时"),
    /**
     * 买家取消关闭
     */
    BUYER_CANCEL(301, "买家取消"),
    /**
     * 卖家取消关闭
     */
    SELLER_CANCEL(302, "卖家取消"),
    /**
     * 换货成功关闭
     */
    EXCHANGE_SUCCESS(303, "换货成功"),

    ;


    /**
     * 值
     */
    @EnumValue
    private final int code;
    /**
     * 描述
     */
    private final String desc;

    OrderEvents(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
