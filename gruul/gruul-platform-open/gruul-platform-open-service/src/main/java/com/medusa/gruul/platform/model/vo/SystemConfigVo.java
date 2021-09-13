package com.medusa.gruul.platform.model.vo;

import com.medusa.gruul.platform.conf.AliyunStorageConfig;
import com.medusa.gruul.platform.conf.QiniouStorageConfig;
import com.medusa.gruul.platform.conf.SystemConfig;
import com.medusa.gruul.platform.conf.TencentStorageConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 */
@Data
public class SystemConfigVo {

    @ApiModelProperty(value = "系统配置值")
    private SystemConfig systemConfig;
    @ApiModelProperty(value = "阿里云配置", hidden = true)
    private AliyunStorageConfig aliyunStorageConfig;
    @ApiModelProperty(value = "七牛云配置", hidden = true)
    private QiniouStorageConfig qiniouStorageConfig;
    @ApiModelProperty(value = "腾讯云配置", hidden = true)
    private TencentStorageConfig tencentStorageConfig;
}
