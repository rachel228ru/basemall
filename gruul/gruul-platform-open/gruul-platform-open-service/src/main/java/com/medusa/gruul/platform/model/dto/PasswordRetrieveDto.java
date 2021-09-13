package com.medusa.gruul.platform.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author whh
 * @description
 * @data: 2020/3/23
 */
@Data
public class PasswordRetrieveDto {

    @ApiModelProperty(value = "商户手机号")
    @NotBlank(message = "商户手机号不能为空")
    private String phone;

    @ApiModelProperty(value = "校验码凭证")
    @NotBlank(message = "商户手机号不能为空")
    private String certificate;

    @ApiModelProperty(value = "新密码")
    private String passwd;

}
