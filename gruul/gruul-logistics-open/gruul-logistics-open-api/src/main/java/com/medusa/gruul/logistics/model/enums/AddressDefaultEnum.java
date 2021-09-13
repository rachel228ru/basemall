package com.medusa.gruul.logistics.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 地址默认枚举
 *
 * @author kyl
 * @since 2019-10-06
 */
@Getter
public enum AddressDefaultEnum {

    /**
     * 不默认
     */
    NO(0, "否"),
    /**
     * 默认
     */
    YES(1, "是");

    @EnumValue
    /**
     * 值
     */
    private final int addressDefault;

    /**
     * 描述
     */
    private final String desc;

    AddressDefaultEnum(int addressDefault, String desc) {
        this.addressDefault = addressDefault;
        this.desc = desc;
    }

}
