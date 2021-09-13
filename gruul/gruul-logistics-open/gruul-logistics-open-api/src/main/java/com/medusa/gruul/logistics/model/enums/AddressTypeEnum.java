package com.medusa.gruul.logistics.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 地址类型枚举
 *
 * @author kyl
 * @since 2019-10-06
 */
@Getter
public enum AddressTypeEnum {

    /**
     * 发货地址
     */
    DEFSEND(1, "发货地址"),
    /**
     * 收货地址
     */
    DEFRECEIVE(2, "收货地址");

    @EnumValue
    /**
     * 值
     */
    private final int addressType;

    /**
     * 描述
     */
    private final String desc;

    AddressTypeEnum(int addressType, String desc) {
        this.addressType = addressType;
        this.desc = desc;
    }

}
