package com.medusa.gruul.payment.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 微信交易类型
 *
 * @author whh
 */
@Getter
@AllArgsConstructor
public enum WxPayEnum {


    /**
     * 微信交易类型枚举
     */
    JSAPI_MINI(1, "JSAPI支付（小程序appId支付）", null),

    Native(2, "Native支付", null),

    APP(3, "app支付", null),

    JSAPI_MP(4, "JSAPI支付（公众号appId支付）", null),

    H5(6, "H5支付", null),

    /**
     * 商户对个人付款
     */
    ENT_PAY(5, "商户对个人付款", null),



    /**
     * ips pay
     */
    IPS_JS_API(101, "IPS JSAPI支付（或小程序支付）", "JSAPI"),

    IPS_Native(102, "Native支付", "NATIVE"),

    IPS_APP(103, "app支付", "APP"),

    IPS_H5(104, "H5支付", "MWEB"),


    /**
     * sxf pay
     */
    SXF_POLYMERIZATION(202, "公众号服务窗", "02"),

    SXF_JS_API(201, "js支付小程序", "03"),


    /**
     * sft pay
     */
    SFT_JS_API(301, "js支付小程序", null),


    /**
     * UNKNOWN
     */
    UNKNOWN(-1, "未知支付", null);


    /**
     * 类型
     */
    private Integer type;

    /**
     * 描述
     */
    private String description;

    /**
     * ips
     */
    private String code;


    public static WxPayEnum getPayByTradeType(Integer type) {
        return Arrays.asList(values()).stream()
                .filter(wxPayEnum -> wxPayEnum.getType().equals(type))
                .findFirst().orElse(WxPayEnum.UNKNOWN);
    }
}
