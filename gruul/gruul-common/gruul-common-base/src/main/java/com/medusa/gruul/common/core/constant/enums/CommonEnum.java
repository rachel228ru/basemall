package com.medusa.gruul.common.core.constant.enums;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>Descrition: 返回状态 枚举类</P>
 * <p>Version: </p>
 * <p>ModifyVersion: </p>
 * <p>Author Mr.zhaozheng a</P>
 * <p>Date: 2019-11-21 13:11</p>
 * <p>Param a</P>
 * <p>return: a</P>
 */
public enum CommonEnum {
    //成功
    SUCCESS_RESPONSE(200, "成功"),
    //失败
    FAILED_RESPONSE(500, "失败");
    @Setter
    @Getter
    private Integer code;
    @Setter
    @Getter
    private String msg;


    CommonEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
