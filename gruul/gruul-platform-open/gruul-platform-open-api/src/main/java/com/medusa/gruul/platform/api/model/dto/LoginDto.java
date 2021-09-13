package com.medusa.gruul.platform.api.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 * @description
 * @data: 2019/11/21
 */
@Data
public class LoginDto {

    @ApiModelProperty(value = "会话密钥")
    private String sessionKey;

    @ApiModelProperty(value = "用户唯一标识")
    private String openId;

    @ApiModelProperty(value = "用户在开放平台的唯一标识符")
    private String unionId;


}
