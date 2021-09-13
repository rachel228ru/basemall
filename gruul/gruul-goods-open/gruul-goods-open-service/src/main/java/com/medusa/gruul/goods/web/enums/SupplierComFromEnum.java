package com.medusa.gruul.goods.web.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 供应商注册来源
 *
 * @author: KYL
 * @since: 2019/11/22
 */
@Getter
public enum SupplierComFromEnum {
    /**
     * 后台注册
     */
    PC(0, "后台注册"),
    /**
     * 小程序
     */
    MP(1, "小程序");

    @EnumValue
    /**
     * 值
     */
    private final int comeFrom;

    /**
     * 描述
     */
    private final String desc;

    SupplierComFromEnum(int comeFrom, String desc) {
        this.comeFrom = comeFrom;
        this.desc = desc;
    }
}
