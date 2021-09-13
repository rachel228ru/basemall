package com.medusa.gruul.payment.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 微信交易类型
 *
 * @author whh
 */
@Getter
@AllArgsConstructor
public enum IpsPayCodeStatusEnum {


    /**
     * 成功 交易类接口，需要同时判断“响应状态”和“交易状态“ 如：【交易成功标志：响应状态=000000、交易状态=SUCCESS】
     */
    SUCCESS("000000", "成功"),

    /**
     * 业务异常  (12位业务错误码、具体以返回为主)
     */
    FAIL("1xxxxxxxxxxx", "业务异常"),

    /**
     * 系统异常
     */
    APP("999999", "系统异常");


    /**
     * code
     */
    private String code;

    /**
     * 描述
     */
    private String description;

}
