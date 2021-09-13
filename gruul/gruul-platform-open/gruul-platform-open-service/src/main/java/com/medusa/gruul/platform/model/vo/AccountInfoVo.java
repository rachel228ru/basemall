package com.medusa.gruul.platform.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author whh
 * @description
 * @data: 2020/3/7
 */
@Data
public class AccountInfoVo implements Serializable {
    private static final long serialVersionUID = 5740795642506439829L;

    @ApiModelProperty(value = "用户id,不返回仅后端使用", hidden = true)
    private Long id;

    @ApiModelProperty(value = "用户名称")
    private String nikeName;

    @ApiModelProperty(value = "头像url")
    private String avatarUrl;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "token")
    private String token;

    @ApiModelProperty(value = "电子发票接收邮箱")
    private String email;

    @ApiModelProperty(value = "是否代理")
    private Boolean isAgent;

    @ApiModelProperty(value = "最后一次进入的店铺,值为空,则未进入过任何店铺")
    private LoginShopInfoVo shopInfoVo;

}
