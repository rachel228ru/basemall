package com.medusa.gruul.platform.model.dto;

import cn.hutool.core.util.StrUtil;
import com.medusa.gruul.common.core.exception.ServiceException;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author whh
 * @description
 * @data: 2020/4/6
 */
@Data
public class PayInfoUpdataDto {

    @ApiModelProperty(value = "更新类型 1-微信支付 2-环迅支付 3-随行支付,4.盛付通 传1仅修改微信支付,大于1修改指定的同时也会修改微信支付")
    @NotNull
    private Integer payType;
    @ApiModelProperty(value = "租户id", hidden = true)
    private String tenantId;
    @ApiModelProperty(value = "店铺表id")
    private Long shopId;
    @ApiModelProperty(value = "商户支付证书路径")
    @NotEmpty(message = "商户支付证书路径不能为空")
    private String certificatesPath;
    @ApiModelProperty(value = "支付秘钥")
    @NotEmpty(message = "支付秘钥不能为空")
    private String mchKey;
    @ApiModelProperty(value = "商户号")
    @NotEmpty(message = "微信商户号不能为空")
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
     * 盛付通终端号
     */
    @ApiModelProperty(value = "盛付通终端号")
    private String sftTerminalId;

    /**
     * 盛付通秘钥
     */
    @ApiModelProperty(value = "盛付通秘钥")
    private String sftMd5;

    /**
     * 盛付通代理商商户编号
     */
    @ApiModelProperty(value = "盛付通代理商商户编号")
    private String sftChannelId;

    /**
     * 盛付通线下交易子商户号
     */
    @ApiModelProperty(value = "盛付通线下交易子商户号")
    private String sftSubMerchantNo;


    /**
     * 校验环迅付
     */
    public void ipsValidate() {
        if (StrUtil.isEmpty(this.getIpsMerCode())) {
            throw new ServiceException("环迅商户号不能为空");
        }
        if (StrUtil.isEmpty(this.getIpsAccCode())) {
            throw new ServiceException("环迅商户账户编号不能为空");
        }
        if (StrUtil.isEmpty(this.getIpsCertificatePsw())) {
            throw new ServiceException("环迅ipsCertificatePsw证书密码不能为空");
        }
        if (StrUtil.isEmpty(this.getIpsRsaPublicKey())) {
            throw new ServiceException("环迅公钥不能为空");
        }
        if (StrUtil.isEmpty(this.getIpsRsaPrivateKey())) {
            throw new ServiceException("环迅私钥不能为空");
        }
        if (StrUtil.isEmpty(this.getIpsAes())) {
            throw new ServiceException("环迅AES秘钥不能为空");
        }
        if (StrUtil.isEmpty(this.getIpsSha())) {
            throw new ServiceException("环迅SHA公钥不能为空");
        }
    }

    /**
     * 校验随行付
     */
    public void sxfValidate() {
        if (StrUtil.isEmpty(this.getSxfOrgId())) {
            throw new ServiceException("随行付合作机构id不能为空");
        }
        if (StrUtil.isEmpty(this.getSxfAccCode())) {
            throw new ServiceException("随行付商户号入驻商户编号不能为空");
        }
        if (StrUtil.isEmpty(this.getSxfCertificatePsw())) {
            throw new ServiceException("随行付商户号证书密码不能为空");
        }
        if (StrUtil.isEmpty(this.getSxfPublic())) {
            throw new ServiceException("随行付商户号公钥不能为空");
        }
        if (StrUtil.isEmpty(this.getSxfPrivateKey())) {
            throw new ServiceException("随行付商户号秘钥不能为空");
        }
    }

    /**
     * 校验随行付
     */
    public void sftValidate() {
//        if (StrUtil.isEmpty(this.getSftMd5())) {
//            throw new ServiceException("盛付通秘钥不能为空");
//        }
//        if (StrUtil.isEmpty(this.getSftChannelId())) {
//            throw new ServiceException("盛付通代理商商户编号不能为空");
//        }
        if (StrUtil.isEmpty(this.getSftTerminalId())) {
            throw new ServiceException("盛付通终端号不能为空");
        }
        if (StrUtil.isEmpty(this.getSftSubMerchantNo())) {
            throw new ServiceException("盛付通线下交易子商户号不能为空");
        }
    }

}
