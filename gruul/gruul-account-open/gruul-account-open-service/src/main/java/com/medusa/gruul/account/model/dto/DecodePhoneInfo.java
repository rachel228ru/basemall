package com.medusa.gruul.account.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author whh
 * @description
 * @data: 2019/11/26
 */
@Data
public class DecodePhoneInfo {

    @ApiModelProperty(value = "加密数据")
    @NotBlank(message = "参数为空")
    private String encryptedData;
    @ApiModelProperty(value = "偏移量")
    @NotBlank(message = "参数为空")
    private String ivStr;
    @ApiModelProperty(value = "登录时返回的sessionKey")
    @NotBlank(message = "参数为空")
    private String sessionKey;
}
