package com.medusa.gruul.platform.conf;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;

/**
 * @author whh
 */
@Data
public class QiniouStorageConfig {

    @NotBlank(message = "七牛绑定的域名不能为空")
    @URL(message = "七牛绑定的域名格式不正确")
    @ApiModelProperty(value = "七牛绑定的域名")
    private String qiniuDomain;

    @ApiModelProperty(value = "七牛路径前缀")
    private String qiniuPrefix;

    @NotBlank(message = "七牛AccessKey不能为空")
    @ApiModelProperty(value = "七牛ACCESS_KEY")
    private String qiniuAccessKey;

    @ApiModelProperty(value = "七牛SECRET_KEY")
    @NotBlank(message = "七牛SecretKey不能为空")
    private String qiniuSecretKey;

    @NotBlank(message = "七牛空间名不能为空")
    @ApiModelProperty(value = "七牛存储空间名")
    private String qiniuBucketName;
}
