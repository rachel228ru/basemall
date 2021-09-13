package com.medusa.gruul.order.api.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.swagger.annotations.ApiModel;
import lombok.Getter;

/**
 * 支付类型枚举
 *
 * @author alan
 * @date 2019/10/1 22:02
 */
@Getter
@ApiModel(value = "支付类型枚举")
public enum PayTypeEnum {

    /**
     * 未支付
     */
    UNPAID(100, "未支付"),

    /**
     * 微信支付
     */
    WECHAT(102, "微信支付"),
    /**
     * 好友代付
     */
    FRIEND(103, "好友代付"),
    /**
     * 微信支付
     */
    WECHAT_H5(104, "微信支付"),
    /**
     * 无需支付
     */
    FREE(999, "无需支付"),

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

    PayTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
