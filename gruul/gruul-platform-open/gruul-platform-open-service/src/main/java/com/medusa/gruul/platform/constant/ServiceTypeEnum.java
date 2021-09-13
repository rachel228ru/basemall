package com.medusa.gruul.platform.constant;

import lombok.Getter;

/**
 * 基础库分类枚举
 *
 * @author whh
 */

@Getter
public enum ServiceTypeEnum {

    /**
     * 通用服务
     */
    UNIVERSAL_SERVICE("universalService", 1),
    /**
     * 定制服务
     */
    COMMISSION_SERVICE("commissionService", 2);

    private String type;
    private Integer value;

    ServiceTypeEnum(String type, Integer value) {
        this.type = type;
        this.value = value;
    }

    public static ServiceTypeEnum findByValue(String type) {
        for (ServiceTypeEnum baseCategoryTypeEnum : ServiceTypeEnum.values()) {
            if (baseCategoryTypeEnum.type.equals(type)) {
                return baseCategoryTypeEnum;
            }
        }
        return null;
    }

}
