package com.medusa.gruul.account.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * @Description: 后台设置会员请求参数
 * @Author: zhaokw29701
 * @Date: 2020/5/5 23:58
 **/
@Data
@Accessors(chain = true)
public class SetMemberDto {

    @ApiModelProperty(value = "用户id")
    @NotBlank(message = "参数为空")
    private String userId;
    @ApiModelProperty(value = "会员等级代码")
    @NotBlank(message = "参数为空")
    private String rankCode;
    @ApiModelProperty(value = "租户id")
    private String tenantId;
    @ApiModelProperty(value = "手机号")
    @NotBlank(message = "参数为空")
    private String phone;
    @ApiModelProperty(value = "验证码")
    @NotBlank(message = "参数为空")
    private String code;
}
