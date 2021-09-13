package com.medusa.gruul.goods.web.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 商品状态枚举
 *
 * @author lcysike
 * @since 2019-10-06
 */
@Getter
public enum ProductStatusEnum {
    /**
     * 下架
     */
    SELL_OFF(0, "下架"),
    /**
     * 上架
     */
    SELL_ON(1, "上架"),
    /**
     * 已售完
     */
    SELL_OUT(2, "已售完");

    @EnumValue
    /**
     * 值
     */
    private final int status;

    /**
     * 描述
     */
    private final String desc;

    ProductStatusEnum(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

}
