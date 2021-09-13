package com.medusa.gruul.account.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author   whh
 */
@Getter
@AllArgsConstructor
public enum MpLoginCodeEnum {


    /**
     * 禁用类型
     */
    MINI_NOT_LOG(201001, "小程序尚未登录"),
    UNIONID_NULL(201002, "限制下单"),
    SHOPID_NULL(201003, "限制下单"),
    DATA_NULL(201004, "数据异常"),
    ;

    /**
     * 授权类型
     */
    private Integer code;

    /**
     * 类型名称
     */
    private String message;
}
