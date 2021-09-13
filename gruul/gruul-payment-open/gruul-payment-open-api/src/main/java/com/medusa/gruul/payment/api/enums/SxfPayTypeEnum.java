package com.medusa.gruul.payment.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author whh
 * @date 2019/11/06
 */

@Getter
@AllArgsConstructor
public enum SxfPayTypeEnum {


    /**
     * 微信支付
     */
    WE_CHAT("WECHAT", "微信支付"),


    /**
     * 支付宝
     */
    ALI_PAY("ALIPAY", "支付宝"),


    /**
     * 银联
     */
    UNION_PAY("UNIONPAY", "银联");


    /**
     * 类型
     */
    private String type;


    /**
     * 描述
     */
    private String desc;

}
