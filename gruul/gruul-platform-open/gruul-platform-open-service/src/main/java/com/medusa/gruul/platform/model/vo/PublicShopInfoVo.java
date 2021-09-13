package com.medusa.gruul.platform.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 * @description
 * @data: 2020/7/24
 */
@Data
public class PublicShopInfoVo {

    @ApiModelProperty(value = "小程序的appId")
    private String miniAppId;

    @ApiModelProperty(value = "小程序原始id")
    private String userName;
}
