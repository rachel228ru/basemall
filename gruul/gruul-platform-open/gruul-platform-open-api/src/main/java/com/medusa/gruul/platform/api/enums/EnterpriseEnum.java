package com.medusa.gruul.platform.api.enums;

import lombok.Getter;

/**
 * @author whh
 * @description
 * @data: 2020/10/22
 */
@Getter
public enum EnterpriseEnum {

    /**
     * 企业类型
     */
    HZ("HZ", "合资"),
    DZ("DZ", "独资"),
    GY("GY", "国有"),
    SY("SY", "私营"),
    QBSYZ("QBSYZ", "全民所有制"),
    JTSYZ("JTSYZ", "集体所有制"),
    GFZ("GFZ", "股份制"),
    YXZR("YXZR", "有限责任"),
    ;
    /**
     * 类型
     */
    private String code;

    /**
     * 描述
     */
    private String description;

    EnterpriseEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
