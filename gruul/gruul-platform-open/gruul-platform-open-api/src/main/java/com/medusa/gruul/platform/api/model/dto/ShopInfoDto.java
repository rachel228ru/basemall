package com.medusa.gruul.platform.api.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 * @description
 * @data: 2020/7/24
 */
@Data
public class ShopInfoDto {

    @ApiModelProperty(value = "店铺首页图片")
    private String logoUrl;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;


    @ApiModelProperty(value = "0审核中，1部署中 2正常 ，3已打烊，4禁用")
    private Integer status;

    @ApiModelProperty(value = "绑定的小程序id")
    private Long bindMiniId;

    @ApiModelProperty(value = "绑定的公众号id")
    private Long bindMpId;
}
