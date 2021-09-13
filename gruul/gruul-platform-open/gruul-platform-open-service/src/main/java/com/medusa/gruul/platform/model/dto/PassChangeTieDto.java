package com.medusa.gruul.platform.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author cqj
 */
@Data
public class PassChangeTieDto {

    @ApiModelProperty(value = "修改的密码")
    private String passwd;

    @ApiModelProperty(value = "校验码")
    private String certificate;

    @ApiModelProperty(value = "手机号")
    private String phone;

}
