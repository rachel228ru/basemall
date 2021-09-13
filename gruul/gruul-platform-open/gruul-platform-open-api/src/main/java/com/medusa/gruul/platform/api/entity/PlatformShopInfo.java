package com.medusa.gruul.platform.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.medusa.gruul.common.data.base.BaseNoTenantEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 店铺信息表
 * </p>
 *
 * @author whh
 * @since 2020-03-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_platform_shop_info")
@ApiModel(value = "PlatformShopInfo对象", description = "店铺信息表")
public class PlatformShopInfo extends BaseNoTenantEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 店铺首页图片
     */
    @ApiModelProperty(value = "店铺首页图片")
    @TableField("logo_url")
    private String logoUrl;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    @TableField("shop_name")
    private String shopName;

    @ApiModelProperty(value = "营业时间,自行分割")
    @TableField("business_hours")
    private String businessHours;

    @ApiModelProperty(value = "店铺电话")
    @TableField("shop_phone")
    private String shopPhone;

    /**
     * 0审核中，1部署中
     * 2正常 ，3已打烊，4禁用
     */
    @ApiModelProperty(value = "0审核中，1部署中 2正常 ，3已打烊，4禁用")
    @TableField("status")
    private Integer status;

    /**
     * 到期时间
     */
    @ApiModelProperty(value = "到期时间")
    @TableField("due_time")
    private LocalDateTime dueTime;

    /**
     * 是否到期 0 未到期 1已到期
     */
    @ApiModelProperty(value = "是否到期 0 未到期 1已到期")
    @TableField("is_due")
    private Integer isDue;

    /**
     * t_platform_shop_template_info 模版id
     */
    @ApiModelProperty(value = "t_platform_shop_template_info 模版id")
    @TableField("shop_template_id")
    private Long shopTemplateId;

    /**
     * t_platform_shop_template_detail 模版版本id
     */
    @ApiModelProperty(value = "t_platform_shop_template_detail 当前使用的模版版本id")
    @TableField("shop_template_detail_id")
    private Long shopTemplateDetailId;

    /**
     * 商户 id t_platform_account_info的id
     */
    @ApiModelProperty(value = "商户id t_platform_account_info的id")
    @TableField("account_id")
    private Long accountId;

    /**
     * 是否同意笔歌协议 0未同意，1已同意
     */
    @ApiModelProperty(value = "是否同意笔歌协议 0未同意，1已同意")
    @TableField("agree_on")
    private Integer agreeOn;

    /**
     * 套餐id
     */
    @ApiModelProperty(value = "当前订购的套餐id")
    @TableField("package_id")
    private Long packageId;

    /**
     * 套餐id
     */
    @ApiModelProperty(value = "当前订购的套餐订单id")
    @TableField("package_order_id")
    private Long packageOrderId;


    /**
     * 是否私有化部署（0否 1是）
     */
    @ApiModelProperty(value = "是否私有化部署（0否 1是）")
    @TableField("is_privatization_deployment")
    private Integer isPrivatizationDeployment;

    /**
     * 店铺租户id
     */
    @ApiModelProperty(value = "店铺租户id")
    @TableField("tenant_id")
    private String tenantId;

    @ApiModelProperty(value = "创建入口 0-admin 1-商家控制台 2-代理商  3-直属渠道商   4-间接渠道商")
    @TableField("create_join")
    private Integer createJoin;

    @ApiModelProperty(value = "绑定的小程序id")
    @TableField("bind_mini_id")
    private Long bindMiniId;

    @ApiModelProperty(value = "绑定的公众号id")
    @TableField("bind_mp_id")
    private Long bindMpId;
    /**
     * 商户支付证书路径
     */
    @ApiModelProperty(value = "商户支付证书路径")
    @TableField("certificate_path")
    @Deprecated
    private String certificatePath;

    /**
     * 支付秘钥
     */
    @ApiModelProperty(value = "支付秘钥")
    @TableField("mch_key")
    @Deprecated
    private String mchKey;

    /**
     * 商户号
     */
    @ApiModelProperty(value = "商户号")
    @TableField("mch_id")
    @Deprecated
    private String mchId;

    @ApiModelProperty(value = "当前支付渠道 1-微信支付 2-环迅支付 3-随行支付")
    @TableField("pay_type")
    private Integer payType;

    @ApiModelProperty(value = "小程序底部打标")
    @TableField("mini_bottom_log")
    private String miniBottomLog;

}
