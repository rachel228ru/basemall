package com.medusa.gruul.goods.web.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 供应商状态枚举
 *
 * @author kyl
 * @since 2019-10-06
 */
@Getter
public enum SupplierStatusEnum {
    /**
     * 已关闭
     */
    CLOSED(0, "已关闭"),
    /**
     * 已审核
     */
    AUDITED(1, "已审核"),
    /**
     * 待审核
     */
    AUDITING(2, "待审核"),
    /**
     * 禁用中
     */
    FORBIDDEN(3, "禁用中");

    @EnumValue
    /**
     * 值
     */
    private final int status;

    /**
     * 描述
     */
    private final String desc;

    SupplierStatusEnum(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
