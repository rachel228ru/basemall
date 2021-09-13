package com.medusa.gruul.platform.constant;

import lombok.Getter;

/**
 * @author whh
 * @description
 * @data: 2020/1/14
 */
@Getter
public enum ErrorCodeEnum {

    /**
     * 用户不存在或无法搜索到用户
     */
    CODE_85001(85001, "用户不存在或无法搜索到用户"),
    /**
     * 用户已绑定
     */
    CODE_85004(85004, "用户已绑定"),
    ;
    private int code;
    private String msg;


    ErrorCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static String findMsgByCode(int code) {
        for (ErrorCodeEnum value : ErrorCodeEnum.values()) {
            if (value.code == code) {
                return value.msg;
            }
        }
        return null;
    }

}
