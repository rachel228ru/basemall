package com.medusa.gruul.platform.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author whh
 * @description
 * @data: 2020/3/10
 */
@Data
public class LoginAccountInfoDetailVo extends AccountInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户剩余余额")
    private BigDecimal balance;

    @ApiModelProperty(value = "账号类型  0-商户账号 ")
    private Integer accountType;

}
