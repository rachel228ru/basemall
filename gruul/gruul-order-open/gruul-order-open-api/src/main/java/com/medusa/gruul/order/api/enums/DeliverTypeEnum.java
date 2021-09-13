package com.medusa.gruul.order.api.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.swagger.annotations.ApiModel;
import lombok.Getter;

/**
 * 配送方式枚举
 *
 * @author alan
 * @date 2019/10/1 22:02
 */
@Getter
@ApiModel(value = "配送方式枚举")
public enum DeliverTypeEnum {

    /**
     * 物流配送
     */
    LOGISTICS(102, "物流配送"),
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


    DeliverTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
