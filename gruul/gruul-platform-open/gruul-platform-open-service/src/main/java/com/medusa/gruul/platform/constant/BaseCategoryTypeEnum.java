package com.medusa.gruul.platform.constant;

import lombok.Getter;

/**
 * 基础库类型枚举
 *
 * @author whh
 */

@Getter
public enum BaseCategoryTypeEnum {
    /**
     * 业务基础库
     */
    BUSINESS("business", 1),
    /**
     * 支撑基础库
     */
    PUBLIC("public", 2);

    private String type;
    private Integer value;

    BaseCategoryTypeEnum(String type, Integer value) {
        this.type = type;
        this.value = value;
    }

    public static BaseCategoryTypeEnum findByValue(String type) {
        for (BaseCategoryTypeEnum baseCategoryTypeEnum : BaseCategoryTypeEnum.values()) {
            if (baseCategoryTypeEnum.type.equals(type)) {
                return baseCategoryTypeEnum;
            }
        }
        return null;
    }

}
