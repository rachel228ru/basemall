package com.medusa.gruul.platform.model.dto;

import com.medusa.gruul.common.core.validator.group.AddGroup;
import com.medusa.gruul.common.core.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author whh
 * @description
 * @data: 2020/10/21
 */
@Data
public class CardBankDto {

    @ApiModelProperty(value = "银行卡相关表id")
    @NotNull(message = "银行卡相关表id不能为空", groups = {UpdateGroup.class})
    private Long bankId;

    @ApiModelProperty(value = "持卡人手机号")
    @NotEmpty(message = "银行卡手机号不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String cardPhone;

    @ApiModelProperty(value = "持卡人姓名")
    @NotEmpty(message = "持卡人姓名不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String cardName;

    @ApiModelProperty(value = "卡号")
    @NotEmpty(message = "银行卡不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String cardNum;

    @ApiModelProperty(value = "银行卡手机验证码凭证")
    @NotEmpty(message = "银行卡手机验证码为空", groups = {AddGroup.class, UpdateGroup.class})
    private String certificate;

    @ApiModelProperty(value = "开户银行")
    @NotEmpty(message = "开户行不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String cardBankName;

}
