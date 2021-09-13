package com.medusa.gruul.platform.model.vo.agent;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 * @description
 * @data: 2020/11/19
 */
@Data
public class CardBankVo {

    @ApiModelProperty(value = "银行卡相关表id")
    private Long bankId;

    @ApiModelProperty(value = "持卡人手机号")
    private String cardPhone;

    @ApiModelProperty(value = "持卡人姓名")
    private String cardName;

    @ApiModelProperty(value = "卡号")
    private String cardNum;

    @ApiModelProperty(value = "开户银行")
    private String cardBankName;
}
