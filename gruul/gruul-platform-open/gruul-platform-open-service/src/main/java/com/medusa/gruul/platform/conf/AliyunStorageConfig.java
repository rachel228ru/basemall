package com.medusa.gruul.platform.conf;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;

/**
 * @author whh
 */
@Data
public class AliyunStorageConfig {
    @NotBlank(message = "阿里云绑定的域名不能为空")
    @URL(message = "阿里云绑定的域名格式不正确")
    @ApiModelProperty(value = "阿里云绑定的域名")
    private String aliyunDomain;

    @ApiModelProperty(value = "阿里云路径前缀")
    private String aliyunPrefix;

    @NotBlank(message = "阿里云EndPoint不能为空")
    @ApiModelProperty(value = "阿里云EndPoint")
    private String aliyunEndPoint;

    @NotBlank(message = "阿里云AccessKeyId不能为空")
    @ApiModelProperty(value = "阿里云AccessKeyId")
    private String aliyunAccessKeyId;

    @NotBlank(message = "阿里云AccessKeySecret不能为空")
    @ApiModelProperty(value = "阿里云AccessKeySecret")
    private String aliyunAccessKeySecret;

    @NotBlank(message = "阿里云BucketName不能为空")
    @ApiModelProperty(value = "阿里云BucketName")
    private String aliyunBucketName;
}
