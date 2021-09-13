package com.medusa.gruul.afs.api.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.swagger.annotations.ApiModel;
import lombok.Getter;


/**
 * The enum Afs order type enum.
 *
 * @author alan
 * @Description: 售后订单类型枚举
 * @date 2019 /10/1 22:02
 */
@Getter
@ApiModel(value = "售后订单类型枚举")
public enum AfsOrderTypeEnum {
    /**
     * Refund afs order type enum.
     */
    REFUND(3, "退款"),

    /**
     * Return refund afs order type enum.
     */
    RETURN_REFUND(5, "退货退款");


    /**
     * 值
     */
    @EnumValue
    private int code;
    /**
     * 描述
     */
    private String desc;

    AfsOrderTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * Is leader apply integer.
     *
     * @return the integer
     */
    public Integer isLeaderApply() {
        return 1;
    }


    /**
     * Is refund boolean.
     *
     * @return the boolean
     */
    public Boolean isRefund() {
        return this.equals(REFUND) || this.equals(RETURN_REFUND);
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        switch (this) {
            case REFUND:
                return "TK";
            case RETURN_REFUND:
                return "TH";
            default:
                return "";
        }
    }
}
