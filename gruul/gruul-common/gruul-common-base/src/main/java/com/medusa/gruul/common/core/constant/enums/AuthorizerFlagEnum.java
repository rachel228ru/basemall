package com.medusa.gruul.common.core.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 小程序授权状态
 * @author whh
 */
@Getter
@AllArgsConstructor
public enum AuthorizerFlagEnum {

    /**
     * 0-未授权
     */
    UNAUTHORIZED(0, "未授权"),

    /**
     *  1-授权
     */
    AUTHENTICATION(1, "授权");


    /**
     * 状态
     */
    private int status;

    /**
     * 描述
     */
    private String description;

}
