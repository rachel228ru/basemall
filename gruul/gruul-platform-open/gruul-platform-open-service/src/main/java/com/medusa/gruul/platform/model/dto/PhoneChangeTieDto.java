package com.medusa.gruul.platform.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author cqj
 */
@Data
public class PhoneChangeTieDto {

    @ApiModelProperty(value = "换绑前手机号")
    private String oldPhone;


    @ApiModelProperty(value = "换绑后手机号")
    private String newPhone;


    @ApiModelProperty(value = "1级校验码凭证")
    private String oneCertificate;

    @ApiModelProperty(value = "二级校验码凭证")
    private String twoCertificate;
}
