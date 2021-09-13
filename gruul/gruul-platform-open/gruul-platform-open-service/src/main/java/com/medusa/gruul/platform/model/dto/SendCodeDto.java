package com.medusa.gruul.platform.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author whh
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendCodeDto {

    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "校验类型,1001-短信登录校验," +
            "1002-账号注册验证手机号," +
            "1003-用户手机号换绑校验," +
            "1004-用户修改密码," +
            "1005-用户信息修改," +
            "1006-用户删除自身店铺," +
            "1007-用户忘记密码找回," +
            "1008-代理申请," +
            "1009-代理银行卡手机号校验," +
            "1010-代理银行卡手机号换绑," +
            "")
    private Integer type;
}
