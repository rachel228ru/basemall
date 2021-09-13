package com.medusa.gruul.common.core.constant.enums;


import lombok.Getter;

/**
 * @author whh
 * @description 当前登录终端
 * @data: 2020/12/21
 */
@Getter
public enum LoginTerminalEnum {

    /**
     * 小程序端
     */
    MINI,
    /**
     * H5页面端
     */
    H5,
    /**
     * pc电脑端
     */
    PC
}
