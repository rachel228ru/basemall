package com.medusa.gruul.platform.model.dto;

import com.medusa.gruul.platform.conf.AliyunStorageConfig;
import com.medusa.gruul.platform.conf.QiniouStorageConfig;
import com.medusa.gruul.platform.conf.SystemConfig;
import com.medusa.gruul.platform.conf.TencentStorageConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

/**
 * 云存储配置信息
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-25 16:12
 */
@Data
public class SaveConfigDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @Range(min = 0, max = 3, message = "类型错误")
    @ApiModelProperty(value = "保存类型 0-域名配置 1：七牛  2：阿里云  3：腾讯云")
    private Integer type;

    @ApiModelProperty(value = "域名配置")
    private SystemConfig systemConfig;
    @ApiModelProperty(value = "阿里云配置")
    private AliyunStorageConfig aliyunStorageConfig;
    @ApiModelProperty(value = "七牛云配置")
    private QiniouStorageConfig qiniouStorageConfig;
    @ApiModelProperty(value = "腾讯云配置")
    private TencentStorageConfig tencentStorageConfig;

}
