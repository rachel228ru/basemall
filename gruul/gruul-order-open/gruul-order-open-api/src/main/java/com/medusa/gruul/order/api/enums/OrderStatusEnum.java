package com.medusa.gruul.order.api.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.swagger.annotations.ApiModel;
import lombok.Getter;


/**
 * 订单状态枚举
 *
 * @author alan
 * @date 2019/10/1 22:02
 */
@Getter
@ApiModel(value = "订单状态枚举")
public enum OrderStatusEnum {
    /**
     * 待付款
     */
    WAIT_FOR_PAY(100, "待买家付款"),
    /**
     * 待发货
     */
    WAIT_FOR_SEND(101, "待卖家发货"),
    /**
     * 配送中
     */
    SHIPPED(102, "配送中"),
    /**
     * 待提货
     */
    WAIT_FOR_PICKUP(103, "等待买家取货"),
    /**
     * 等待评价
     */
    WAIT_FOR_COMMENT(104, "等待评价"),
    /**
     * 已完成
     */
    COMPLETE(105, "订单已完成"),
    /**
     * 已退款
     */
    REFUNDED(200, "订单退货或全额退款"),
    /**
     * 部分退款
     */
    PART_REFUNDED(201, "部分退款"),
    /**
     * 支付超时关闭
     */
    BUYER_PAY_TIMEOUT_CLOSE(300, "支付超时关闭"),
    /**
     * 买家取消关闭
     */
    BUYER_CANCEL_CLOSE(301, "买家取消关闭"),
    /**
     * 卖家取消关闭
     */
    SELLER_CANCEL_CLOSE(302, "卖家取消关闭"),
    /**
     * 换货成功关闭
     */
    EXCHANGE_SUCCESS_CLOSE(303, "换货成功关闭"),
    /**
     * 换货关闭
     */
    EXCHANGE_CANCEL_CLOSE(304, "换货关闭"),
    ;


    /**
     * 值
     */
    @EnumValue
    private int code;
    /**
     * 描述
     */
    private String desc;

    OrderStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static boolean isClose(OrderStatusEnum status) {
        if (status == EXCHANGE_SUCCESS_CLOSE
                || status == EXCHANGE_CANCEL_CLOSE
                || status == SELLER_CANCEL_CLOSE
                || status == BUYER_CANCEL_CLOSE
                || status == BUYER_PAY_TIMEOUT_CLOSE
                || status == REFUNDED
                || status == COMPLETE
        ) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public static boolean canCancel(OrderStatusEnum status) {
        if (status == WAIT_FOR_PAY) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public static boolean canChangeReceiverAddress(OrderStatusEnum status) {
        if (status == WAIT_FOR_SEND) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public static boolean canVerify(OrderStatusEnum status) {
        if (status == SHIPPED) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public static boolean canReceipt(OrderStatusEnum status) {
        if (status == WAIT_FOR_PICKUP) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public static boolean canEvaluate(OrderStatusEnum status) {
        if (status == WAIT_FOR_COMMENT) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public static boolean canPay(OrderStatusEnum status) {
        if (status == WAIT_FOR_PAY) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public static boolean canCompleted(OrderStatusEnum status) {
        if (status == WAIT_FOR_COMMENT) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
}
