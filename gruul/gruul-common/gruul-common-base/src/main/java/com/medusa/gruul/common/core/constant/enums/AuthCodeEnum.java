package com.medusa.gruul.common.core.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 短信验证码
 *
 * @author whh
 */
@Getter
@AllArgsConstructor
public enum AuthCodeEnum {

    /* 平台模块1000-2000  start================>*/
    MINI_LOGIN(1001, "短信登录校验"),
    CREATE_MINI_REGISTER(1002, "注册时手机号校验"),
    ACCOUNT_PHONE_IN_TIE(1003, "用户手机号换绑校验"),
    ACCOUNT_PASSWORD_IN_TIE(1004, "用户修改密码"),
    ACCOUNT_INFO_MOTIFY(1005, "用户信息修改"),
    ACCOUNT_DEL_ACCOUNT(1006, "用户删除自身店铺"),
    ACCOUNT_FORGET_PASSWD(1007, "用户忘记密码找回"),
    AGENT_APPLYFOR(1008, "代理申请"),
    AGENT_BANK_VALIDATE(1009, "代理银行卡手机号校验"),
    AGENT_TRADE_PHONE(1010, "代理银行卡手机号换绑"),
    AGENT_PHONE_INTIE(1011, "代理账号换绑"),
    AGENT_PASSWORD_INTIE(1012, "代理修改密码"),



    /* 平台模块1000-2000   end================>*/

    /**
     * 认证失败
     */
    AUTH_FAIL(3, "认证失败"),

    /**
     * 过期jwt
     */
    AUTH_EXPIRES(4, "认证过期"),
    ;
    /**
     * 类型
     */
    private int type;

    /**
     * 描述
     */
    private String description;

    public static boolean isExistValue(Integer type) {
        for (AuthCodeEnum value : AuthCodeEnum.values()) {
            if (value.getType() == type) {
                return true;
            }
        }
        return false;
    }


}
