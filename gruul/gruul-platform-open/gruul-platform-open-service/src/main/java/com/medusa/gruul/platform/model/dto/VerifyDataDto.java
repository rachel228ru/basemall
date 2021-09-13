package com.medusa.gruul.platform.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author whh
 * @description
 * @data: 2020/12/21
 */
@Data
public class VerifyDataDto {
    @ApiModelProperty("手机号")
    private String phone;
    @NotNull(message = "校验类型错误")
    @ApiModelProperty("1.校验给定手机号是否当前用户手机号")
    private Integer option;
}
