package com.medusa.gruul.platform.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author whh
 * @description
 * @data: 2020/3/7
 */
@Data
public class LoginDto {

    @ApiModelProperty(value = "登录类型 1-密码登录  2-验证码登录 3-扫码登录 ")
    @NotNull
    private Integer loginType;
    @ApiModelProperty(value = "登录账号")
    private String phone;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "校验码凭证")
    private String certificate;
    @ApiModelProperty(value = "预扫码回调之后返回的code")
    private String code;
}
