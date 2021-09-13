package com.medusa.gruul.platform.api.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.medusa.gruul.common.data.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author whh
 */
@ApiModel(value = "com-medusa-gruul-platform-api-entity-TPlatformPayConfig")
@Data
@TableName("t_platform_pay_config")
@EqualsAndHashCode(callSuper = true)
public class PlatformPayConfig extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1863199597655077657L;
    @ApiModelProperty(value = "")
    private Long id;

    /**
     * 商户支付证书路径
     */
    @ApiModelProperty(value = "商户支付证书路径")
    private String certificatePath;

    /**
     * 支付秘钥
     */
    @ApiModelProperty(value = "支付秘钥")
    private String mchKey;

    /**
     * 商户号
     */
    @ApiModelProperty(value = "商户号")
    private String mchId;

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
    @ApiModelProperty(value = "环迅ipsCertificatePsw证书密码")
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