package com.medusa.gruul.platform.model.dto.agent;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 * @description
 * @data: 2020/11/15
 */
@Data
public class AgentInfoMotifyDto {

    @ApiModelProperty(value = "联系人")
    private String linkName;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "手机号验证码凭证")
    private String phoneCodeCertificate;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "密码验证码凭证")
    private String passwordCodeCertificate;
}
