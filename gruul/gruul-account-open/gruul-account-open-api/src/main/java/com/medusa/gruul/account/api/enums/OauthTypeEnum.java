package com.medusa.gruul.account.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author whh
 * @date 2019/11/06
 */

@Getter
@AllArgsConstructor
public enum OauthTypeEnum {

    /**
     * 授权类型
     */
    WX_MINI(1, "微信小程序"),
    WX_MP(2, "微信公众号");

    /**
     * 授权类型
     */
    private Integer type;

    /**
     * 类型名称
     */
    private String name;

}
