package com.medusa.gruul.platform.model.dto;

import cn.hutool.core.util.NumberUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.RoundingMode;

/**
 * @author whh
 * @description
 * @data: 2020/3/7
 */
@Data
public class AccountRegisterDto {

    @ApiModelProperty(value = "注册手机号")
    @NotBlank(message = "注册手机号不能为空")
    private String phone;
    @ApiModelProperty(value = "校验码凭证")
    @NotBlank(message = "校验码错误")
    private String certificate;
    @ApiModelProperty(value = "登录密码")
    @NotBlank(message = "登录密码不能为空")
    private String password;
    @ApiModelProperty(value = "地区")
    @NotBlank(message = "地区不能为空")
    private String region;

    @ApiModelProperty(value = "住址")
    @NotBlank(message = "住址不能为空")
    private String address;

    @ApiModelProperty(value = "扫码回调成功之后url上返回的code")
    @NotBlank(message = "授权码不能为空")
    private String code;


}
