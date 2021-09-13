package com.medusa.gruul.platform.conf;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author whh
 */
@Data
public class TencentStorageConfig {

    @NotBlank(message = "腾讯云绑定的域名不能为空")
    @URL(message = "腾讯云绑定的域名格式不正确")
    @ApiModelProperty(value = "腾讯云绑定的域名")
    private String qcloudDomain;

    @ApiModelProperty(value = "腾讯云路径前缀")
    private String qcloudPrefix;

    @NotNull(message = "腾讯云AppId不能为空")
    @ApiModelProperty(value = "腾讯云AppId")
    private Integer qcloudAppId;

    @NotBlank(message = "腾讯云SecretId不能为空")
    @ApiModelProperty(value = "腾讯云SecretId")
    private String qcloudSecretId;

    @ApiModelProperty(value = "腾讯云AppId")
    @NotBlank(message = "腾讯云SecretKey")
    private String qcloudSecretKey;

    @ApiModelProperty(value = "腾讯云AppId")
    @NotBlank(message = "腾讯云BucketName")
    private String qcloudBucketName;

    @ApiModelProperty(value = "腾讯云AppId")
    @NotBlank(message = "腾讯云COS所属地区")
    private String qcloudRegion;
}
