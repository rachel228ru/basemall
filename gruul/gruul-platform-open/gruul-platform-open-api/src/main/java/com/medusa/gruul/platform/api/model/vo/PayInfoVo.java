package com.medusa.gruul.platform.api.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 * @description
 * @data: 2020/4/6
 */
@Data
public class PayInfoVo {

    @ApiModelProperty(value = "微信支付商户号秘钥")
    private String mchKey;
    @ApiModelProperty(value = "微信支付商户号")
    private String mchId;

    @ApiModelProperty(value = "支付证书上传状态   false-未上传  true-已上传")
    private Boolean certificatesState;

    @ApiModelProperty(value = "支付证书,仅在内部调用时返回", hidden = true)
    private String certificatesPath;

    @ApiModelProperty(value = "当前支付渠道类型 1-微信支付 2-环迅支付 3-随行支付")
    private Integer payType;

    /**
     * 环迅商户号
     */
    @ApiModelProperty(value = "环迅商户号")
    private String ipsMerCode;

    /**
     * 环迅商户账户编号
     */
    @ApiModelProperty(value = "环迅商户账户编号")
    private String ipsAccCode;

    /**
     * 环迅ipsCertificatePsw证书密码
     */
    @ApiModelProperty(value = "环迅证书密码")
    private String ipsCertificatePsw;

    /**
     * 环迅公钥
     */
    @ApiModelProperty(value = "环迅公钥")
    private String ipsRsaPublicKey;

    /**
     * 环迅私钥
     */
    @ApiModelProperty(value = "环迅私钥")
    private String ipsRsaPrivateKey;

    /**
     * 环迅AES秘钥
     */
    @ApiModelProperty(value = "环迅AES秘钥")
    private String ipsAes;

    /**
     * 环迅SHA公钥
     */
    @ApiModelProperty(value = "环迅SHA公钥")
    private String ipsSha;

    /**
     * 随行付合作机构id
     */
    @ApiModelProperty(value = "随行付合作机构id")
    private String sxfOrgId;

    /**
     * 随行付商户号入驻商户编号
     */
    @ApiModelProperty(value = "随行付商户号入驻商户编号")
    private String sxfAccCode;

    /**
     * 随行付商户号证书密码
     */
    @ApiModelProperty(value = "随行付商户号证书密码")
    private String sxfCertificatePsw;

    /**
     * 随行付商户号公钥
     */
    @ApiModelProperty(value = "随行付商户号公钥")
    private String sxfPublic;

    /**
     * 随行付户号秘钥
     */
    @ApiModelProperty(value = "随行付商户号秘钥")
    private String sxfPrivateKey;


    /**
     * 随行付户号秘钥
     */
    @ApiModelProperty(value = "盛付通终端号")
    private String sftTerminalId;

    /**
     * 随行付户号秘钥
     */
    @ApiModelProperty(value = "盛付通秘钥")
    private String sftMd5;

    /**
     * 随行付户号秘钥
     */
    @ApiModelProperty(value = "盛付通代理商商户编号")
    private String sftChannelId;

    /**
     * 随行付户号秘钥
     */
    @ApiModelProperty(value = "盛付通线下交易子商户号")
    private String sftSubMerchantNo;
}
