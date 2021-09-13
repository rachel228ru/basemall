package com.medusa.gruul.platform.api.enums;

import lombok.Getter;

/**
 * @author whh
 * @description
 * @data: 2020/10/22
 */
@Getter
public enum CompanyScaleEnum {

    /**
     * 公司规模
     */
    SIZE_10("10", "0-10人"),
    SIZE_30("30", "10-30人"),
    SIZE_50("50", "30-50人"),
    SIZE_80("80", "50-80人"),
    SIZE_100("100", "80-100人"),
    SIZE_500("500", "100-500人"),
    SIZE_501("501", "500以上"),
    ;

    /**
     * 类型
     */
    private String code;

    /**
     * 描述
     */
    private String description;

    CompanyScaleEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
