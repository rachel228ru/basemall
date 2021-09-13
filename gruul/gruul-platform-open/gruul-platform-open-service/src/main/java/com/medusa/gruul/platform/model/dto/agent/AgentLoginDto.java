package com.medusa.gruul.platform.model.dto.agent;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author Huihuang Wu
 * @description
 * @data: 2020/10/30
 */
@Data
public class AgentLoginDto {

    @ApiModelProperty(value = "账号")
    @NotEmpty(message = "账号未输入")
    private String account;
    @ApiModelProperty(value = "密码")
    @NotEmpty(message = "代理密码不能为空")
    private String password;
}
