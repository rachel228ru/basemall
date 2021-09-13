package com.medusa.gruul.platform.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 */
@Data
public class VerifyPhoneDto {

    @ApiModelProperty(value = "商家绑定手机号")
    private String phone;
    @ApiModelProperty(value = "验证码")
    private String code;
}
