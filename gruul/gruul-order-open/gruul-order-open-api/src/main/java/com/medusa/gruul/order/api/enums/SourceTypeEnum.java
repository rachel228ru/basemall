package com.medusa.gruul.order.api.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.swagger.annotations.ApiModel;
import lombok.Getter;

/**
 * 订单来源枚举
 *
 * @author alan
 * @date 2019/10/1 22:02
 */
@Getter
@ApiModel(value = "订单来源枚举")
public enum SourceTypeEnum {

    /**
     * 小程序
     */
    MINI_PROGRAM(100, "小程序"),

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

    SourceTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
