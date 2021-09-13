package com.medusa.gruul.common.dto;

import com.medusa.gruul.common.core.constant.enums.LoginTerminalEnum;
import lombok.Data;

/**
 * @author whh
 * @description
 * @data: 2021/1/27
 */
@Data
public class CurPcUserInfoDto {

    /**
     * 商户id
     */
    private String userId;

    /**
     * 当前登录终端
     */
    private LoginTerminalEnum terminalType;

    /**
     * 用户名称
     */
    private String nikeName;

    /**
     * 头像url
     */
    private String avatarUrl;

    /**
     * 性别 0：未知、1：男、2：女
     */
    private Integer gender;

    /**
     * 小程序openId
     */
    private String openId;

    /**
     * 是否代理
     */
    private Boolean isAgent;

}
