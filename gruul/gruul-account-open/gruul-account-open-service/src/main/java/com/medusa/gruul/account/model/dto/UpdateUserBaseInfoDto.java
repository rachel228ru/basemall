package com.medusa.gruul.account.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 * @description
 * @data: 2019/11/27
 */
@Data
public class UpdateUserBaseInfoDto {
    @ApiModelProperty(value = "昵称")
    private String nickName;
    @ApiModelProperty(value = "预留昵称字段")
    private String nikeName;
    @ApiModelProperty(value = "性别")
    private int gender;
    @ApiModelProperty(value = "语言")
    private String language;
    @ApiModelProperty(value = "城市")
    private String city;
    @ApiModelProperty(value = "省")
    private String province;
    @ApiModelProperty(value = "国家")
    private String country;
    @ApiModelProperty(value = "头像")
    private String avatarUrl;
}
