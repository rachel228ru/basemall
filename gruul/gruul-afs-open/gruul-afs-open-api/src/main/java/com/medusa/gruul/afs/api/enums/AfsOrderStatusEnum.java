package com.medusa.gruul.afs.api.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.swagger.annotations.ApiModel;
import lombok.Getter;


/**
 * The enum Afs order status enum.
 *
 * @author alan
 * @Description: 订单状态枚举
 * @date 2019 /10/1 22:02
 */
@Getter
@ApiModel(value = "售后订单状态枚举")
public enum AfsOrderStatusEnum {
    /**
     * Wait for business approved afs order status enum.
     */
    WAIT_FOR_BUSINESS_APPROVED(1, "待商家审核"),
    /**
     * Wait for return afs order status enum.
     */
    WAIT_FOR_RETURN(2, "待退货"),
    /**
     * Wait for business receipt afs order status enum.
     */
    WAIT_FOR_BUSINESS_RECEIPT(4, "待商家确认收货"),
    /**
     * Wait for send afs order status enum.
     */
    WAIT_FOR_SEND(5, "待发货"),
    /**
     * Shipped afs order status enum.
     */
    SHIPPED(6, "配送中"),
    /**
     * Wait for pickup afs order status enum.
     */
    WAIT_FOR_PICKUP(7, "待提货"),
    /**
     * Success afs order status enum.
     */
    SUCCESS(100, "成功"),
    /**
     * Close afs order status enum.
     */
    CLOSE(999, "关闭");


    /**
     * 值
     */
    @EnumValue
    private int code;
    /**
     * 描述
     */
    private String desc;

    AfsOrderStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * Can receipt boolean.
     *
     * @return the boolean
     */
    public boolean canReceipt() {
        return this.equals(WAIT_FOR_BUSINESS_APPROVED)
                || this.equals(WAIT_FOR_BUSINESS_RECEIPT)
                || this.equals(WAIT_FOR_RETURN)
                || isEnd();

    }

    /**
     * Is end boolean.
     *
     * @return the boolean
     */
    public boolean isEnd() {
        return this.equals(SUCCESS) || this.equals(CLOSE);

    }

    /**
     * Is business receipted boolean.
     *
     * @return the boolean
     */
    public boolean isBusinessReceipted() {
        return this.equals(WAIT_FOR_BUSINESS_RECEIPT);

    }

    /**
     * Is business approve boolean.
     *
     * @return the boolean
     */
    public boolean isBusinessApprove() {
        return this.equals(WAIT_FOR_BUSINESS_APPROVED);
    }

    /**
     * Wait seller approve boolean.
     *
     * @return the boolean
     */
    public boolean waitSellerApprove() {
        return isBusinessApprove() || isBusinessReceipted();
    }
}
