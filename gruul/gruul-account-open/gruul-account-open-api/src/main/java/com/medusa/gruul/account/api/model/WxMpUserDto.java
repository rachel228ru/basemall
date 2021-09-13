package com.medusa.gruul.account.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 * @description
 * @data: 2020/7/21
 */
@Data
public class WxMpUserDto {

    @ApiModelProperty(value = "公众号用户openId")
    private String openId;

    @ApiModelProperty(value = "关联unionId")
    private String unionId;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "性别描述信息：男、女、未知等.")
    private String sexDesc;

    @ApiModelProperty(value = "性别表示：1，2等数字.")
    private Integer sex;

    @ApiModelProperty(value = "语言")
    private String language;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "省份")
    private String province;

    @ApiModelProperty(value = "国家")
    private String country;

    @ApiModelProperty(value = "头像")
    private String headImgUrl;
    @ApiModelProperty(value = "租户id")
    private String tenantId;
    @ApiModelProperty(name = "店铺id")
    private String shopId;
}
