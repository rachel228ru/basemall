package com.medusa.gruul.payment.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author whh
 * @date 2019/11/06
 */

@Getter
@AllArgsConstructor
public enum PayChannelEnum {
    /**
     * 审核通过
     */
    WX(1, "微信支付"),

    /**
     * 环迅
     */
    IPS(2, "环迅支付");



    /**
     * 类型
     */
    private Integer type;

    /**
     * 描述
     */
    private String description;
}
