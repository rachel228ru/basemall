package com.medusa.gruul.platform.conf;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 * @description
 * @data: 2020/3/29
 */
@Data
public class SystemConfig {

    @ApiModelProperty(value = "小程序请求域名")
    private String miniDomain;

    @ApiModelProperty(value = "控制台地址")
    private String consoleUrl;

    @ApiModelProperty(value = "官网地址")
    private String consoleLog;

    @ApiModelProperty(value = "代理后台地址")
    private String agentUrl;

}
