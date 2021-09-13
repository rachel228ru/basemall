package com.medusa.gruul.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 */
@Data
public class SendCodeVerifyDto {

    @ApiModelProperty(value = "验证码")
    private String  code;

    @ApiModelProperty(value = "手机号")
    private String  phone;

    @ApiModelProperty(value = "校验类型")
    private Integer  type;

    public SendCodeVerifyDto() {
    }

    public SendCodeVerifyDto(String code, String phone, Integer type) {
        this.code = code;
        this.phone = phone;
        this.type = type;
    }
}
