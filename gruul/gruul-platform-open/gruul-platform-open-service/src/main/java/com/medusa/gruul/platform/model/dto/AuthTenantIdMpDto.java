package com.medusa.gruul.platform.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author whh
 * @description
 * @data: 2020/9/26
 */
@Data
public class AuthTenantIdMpDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "授权之后业务回调的地址")
    private String redirectUri;

    @ApiModelProperty(value = "授权作用域，拥有多个作用域用逗号（,）分隔，已微信认证的服务号拥有 snsapi_base 和 snsapi_userinfo，默认snsapi_userinfo")
    private String scope;

    @ApiModelProperty(value = "租户id")
    private String tenantId;

    @ApiModelProperty(value = "店铺id")
    private String shopId;

    @ApiModelProperty(value = "微信回调授权code,回调流程内部生成使用", hidden = true)
    private String code;

    @ApiModelProperty(value = "公众号信息表id,回调流程内部生成使用", hidden = true)
    private Long mpInfoId;
}
