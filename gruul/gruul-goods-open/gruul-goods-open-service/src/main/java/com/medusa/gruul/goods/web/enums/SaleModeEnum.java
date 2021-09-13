package com.medusa.gruul.goods.web.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 销售专区枚举
 *
 * @author kyl
 * @since 2019-10-06
 */
@Getter
public enum SaleModeEnum {
    /**
     * 商超系统
     */
    SUPERMARKET(0, "商超系统"),
    /**
     * 限时秒杀
     */
    FLASH_SALE(2, "限时秒杀");

    @EnumValue
    /**
     * 值
     */
    private final int saleMode;

    /**
     * 描述
     */
    private final String desc;

    SaleModeEnum(int saleMode, String desc) {
        this.saleMode = saleMode;
        this.desc = desc;
    }

}
