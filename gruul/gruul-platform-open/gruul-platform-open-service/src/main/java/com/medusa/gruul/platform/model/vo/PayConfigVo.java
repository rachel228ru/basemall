package com.medusa.gruul.platform.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 * @description
 * @data: 2020/8/1
 */
@Data
public class PayConfigVo {

    @ApiModelProperty(value = "微信支付appId")
    private String wxAppId;
    @ApiModelProperty(value = "微信支付商户秘钥")
    private String wxMchKey;
    @ApiModelProperty(value = "微信支付商户号")
    private String wxMchId;
    @ApiModelProperty(value = "阿里支付应用appId")
    private String alipayAppId;
    @ApiModelProperty(value = "支付包应用私钥")
    private String alipayPrivateKey;
    @ApiModelProperty(value = "支付宝公钥")
    private String alipayPublicKey;
    @ApiModelProperty(value = "户名")
    private String name;
    @ApiModelProperty(value = "开户行")
    private String bankOfDeposit;
    @ApiModelProperty(value = "银行卡")
    private String bankCard;
}
