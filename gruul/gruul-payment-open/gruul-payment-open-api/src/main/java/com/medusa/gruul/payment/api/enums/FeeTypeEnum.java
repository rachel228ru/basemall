package com.medusa.gruul.payment.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author whh
 * @date 2019/11/06
 */

@Getter
@AllArgsConstructor
public enum FeeTypeEnum {

    /**
     * 货币类型 CNY：人民币(默认使用人民币作为单位)
     */
    CNY("CNY", "人民币(默认使用人民币作为单位)", "156");

    /**
     * 名称
     */
    private String name;

    /**
     * 货币描述
     */
    private String description;


    /**
     * ips 货币类型
     */
    private String ipsCode;
}
