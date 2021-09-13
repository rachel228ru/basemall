package com.medusa.gruul.account.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 * @description
 * @data: 2019/11/19
 */
@Data
@ApiModel(value = "登录时返回的基本信息")
public class LoginBaseInfoVo {

    @ApiModelProperty(value = "用户token")
    private String token;

    @ApiModelProperty(value = "是否授权过小程序 false 未授权  turn授权过,具体授权之后是否取消或者失效,调用微信开放接口查询")
    private Boolean whetherAuthorization;

    @ApiModelProperty(value = "校验用户当前 session_key 是否有效时需要用到,已经获取用户授权信息解密时需要用到")
    private String sessionKey;

}
