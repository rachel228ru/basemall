package com.medusa.gruul.platform.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author smzr
 * @description
 * @data: 2020/7/18
 */
@Setter
@Getter
public class WxJdkConfigVo {

    @ApiModelProperty(value = "签名的时间戳")
    private long timestamp;
    @ApiModelProperty(value = "签名的随机串")
    private String nonceStr;
    @ApiModelProperty(value = "签名")
    private String signature;
    @ApiModelProperty(value = "公众号的appId")
    private String appId;
    @ApiModelProperty(value = "店铺对应的小程序id")
    private String miniAppId;
}
