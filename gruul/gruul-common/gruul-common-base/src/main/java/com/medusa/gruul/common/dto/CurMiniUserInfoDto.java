package com.medusa.gruul.common.dto;

import com.medusa.gruul.common.core.constant.enums.LoginTerminalEnum;
import lombok.Data;

/**
 * @author whh
 * @description
 * @data: 2021/1/27
 */
@Data
public class CurMiniUserInfoDto {

    /**
     * 用户系统唯一id
     * userId 与 shopUserId关系为:1-N
     * 即一个小程序就一个用户系统唯一id，但有多个shopUserId
     * 不管是在多店铺还是当店铺的情况下都用shopUserId，除非是需要数据互通,共享,可多取userId来表示用户关联数据
     */
    private String userId;

    /**
     * 用户所在店铺唯一id
     */
    private String shopUserId;

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
}
