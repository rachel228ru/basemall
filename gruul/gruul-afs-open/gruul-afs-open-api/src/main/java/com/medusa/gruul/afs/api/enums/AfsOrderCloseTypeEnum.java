package com.medusa.gruul.afs.api.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.swagger.annotations.ApiModel;
import lombok.Getter;


/**
 * The enum Afs order close type enum.
 *
 * @author alan
 * @Description: 售后订单类型枚举
 * @date 2019 /10/1 22:02
 */
@Getter
@ApiModel(value = "售后订单类型枚举")
public enum AfsOrderCloseTypeEnum {
    /**
     * User cancel afs order close type enum.
     */
    USER_CANCEL(1, "用户撤销"),
    /**
     * Seller refuse afs order close type enum.
     */
    SELLER_REFUSE(5, "卖家拒绝"),
    /**
     * Re apply afs order close type enum.
     */
    RE_APPLY(6, "重新申请售后");


    /**
     * 值
     */
    @EnumValue
    private int code;
    /**
     * 描述
     */
    private String desc;

    AfsOrderCloseTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
