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
 * 小程序基本信息(非授权时获取的)
 * </p>
 *
 * @author whh
 * @since 2019-09-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_mini_info")
@ApiModel(value = "MiniInfo对象")
public class MiniInfo extends BaseNoTenantEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id ")
    @TableField("tenant_id")
    private String tenantId;

    /**
     * 拥有者id
     */
    @ApiModelProperty(value = "拥有者id")
    @TableField("account_id")
    private Long accountId;

    /**
     * 禁用状态 0-未禁用 1-已禁用
     */
    @ApiModelProperty(value = "是否禁用  0-未禁用 1-已禁用")
    @TableField("forbid_status")
    private Integer forbidStatus;

    /**
     * 是否默认 0否 1默认
     */
    @ApiModelProperty(value = "是否默认 0否 1默认")
    @TableField("default_status")
    private Integer defaultStatus;

    /**
     * 小程序名称
     */
    @ApiModelProperty(value = "小程序名称")
    @TableField("mini_name")
    private String miniName;

    /**
     * appId
     */
    @ApiModelProperty(value = "appId")
    @TableField("app_id")
    private String appId;

    /**
     * 用以了解以下功能的开通状况（0代表未开通，1代表已开通）： open_store:是否开通微信门店功能 open_scan:是否开通微信扫商品功能 open_pay:是否开通微信支付功能
     * open_card:是否开通微信卡券功能 open_shake:是否开通微信摇一摇功能
     */
    @ApiModelProperty(
            value = "用以了解以下功能的开通状况（0代表未开通，1代表已开通）： open_store:是否开通微信门店功能 open_scan:是否开通微信扫商品功能 open_pay:是否开通微信支付功能 open_card:是否开通微信卡券功能 open_shake:是否开通微信摇一摇功能")
    @TableField("business_info")
    private String businessInfo;

    /**
     * 主体类型（1：企业）
     */
    @ApiModelProperty(value = "主体类型（1：企业）")
    @TableField("principal_type")
    private Integer principalType;

    /**
     * 主体名称
     */
    @ApiModelProperty(value = "主体名称")
    @TableField("principal_name")
    private String principalName;

    /**
     * 实名验证状态（1：实名验证成功，2：实名验证中，3：实名验证失败） 0-false 1-true
     */
    @ApiModelProperty(value = "实名验证状态（1：实名验证成功，2：实名验证中，3：实名验证失败） 0-false 1-true")
    @TableField("realname_status")
    private Integer realnameStatus;

    /**
     * 是否资质认证（true：是，false：否）若是，拥有微信认证相关的权限。
     */
    @ApiModelProperty(value = "是否资质认证（true：是，false：否）若是，拥有微信认证相关的权限。")
    @TableField("qualification_verify")
    private Boolean qualificationVerify;

    /**
     * 是否名称认证（true：是，false：否）对于公众号（订阅号、服务号），是名称认证，微信客户端才会有微信认证打勾的标识。
     */
    @ApiModelProperty(value = "是否名称认证（true：是，false：否）对于公众号（订阅号、服务号），是名称认证，微信客户端才会有微信认证打勾的标识。")
    @TableField("naming_verify")
    private Boolean namingVerify;

    /**
     * 是否需要年审（true：是，false：否）（qualification_verify = true时才有该字段）
     */
    @ApiModelProperty(value = "是否需要年审（true：是，false：否）（qualification_verify = true时才有该字段）")
    @TableField("annual_review")
    private Boolean annualReview;

    /**
     * 年审开始时间，时间戳（qualification_verify = true时才有该字段）
     */
    @ApiModelProperty(value = "年审开始时间，时间戳（qualification_verify = true时才有该字段）")
    @TableField("annual_review_begin_time")
    private LocalDateTime annualReviewBeginTime;

    /**
     * 年审截止时间，时间戳（qualification_verify = true时才有该字段）
     */
    @ApiModelProperty(value = "年审截止时间，时间戳（qualification_verify = true时才有该字段）")
    @TableField("annual_review_end_time")
    private LocalDateTime annualReviewEndTime;

    /**
     * 功能介绍
     */
    @ApiModelProperty(value = "功能介绍")
    @TableField("signature")
    private String signature;

    /**
     * 功能介绍已使用修改次数（本月）
     */
    @ApiModelProperty(value = "功能介绍已使用修改次数（本月）")
    @TableField("signature_modify_used_count")
    private Integer signatureModifyUsedCount;

    /**
     * 功能介绍修改次数总额度（本月）
     */
    @ApiModelProperty(value = "功能介绍修改次数总额度（本月）")
    @TableField("signature_modify_quota")
    private Integer signatureModifyQuota;

    /**
     * 头像url
     */
    @ApiModelProperty(value = "头像url")
    @TableField("head_image_url")
    private String headImageUrl;

    /**
     * 头像已使用修改次数（本年）
     */
    @ApiModelProperty(value = "头像已使用修改次数（本年）")
    @TableField("head_modify_used_count")
    private Integer headModifyUsedCount;

    /**
     * 头像修改次数总额度（本年）
     */
    @ApiModelProperty(value = "头像修改次数总额度（本年）")
    @TableField("head_modify_quota")
    private Integer headModifyQuota;


    /**
     * 小程序码
     */
    @ApiModelProperty(value = "小程序码")
    @TableField("mini_code")
    private String miniCode;

    /**
     * 小程序二维码
     */
    @ApiModelProperty(value = "小程序二维码")
    @TableField("qrcode")
    private String qrcode;

    /**
     * 体验二维码
     */
    @ApiModelProperty(value = "体验二维码")
    @TableField("experience_code")
    private String experienceCode;

    /**
     * 商户支付证书路径
     *
     * @Deprecated 使用remoteMiniInfoService.getShopConfig()
     */
    @ApiModelProperty(value = "商户支付证书路径")
    @TableField(exist = false)
    @Deprecated
    private String certificatePath;

    /**
     * 支付秘钥
     *
     * @Deprecated 使用remoteMiniInfoService.getShopConfig()
     */
    @ApiModelProperty(value = "支付秘钥")
    @TableField(exist = false)
    @Deprecated
    private String mchKey;

    /**
     * 商户号
     *
     * @Deprecated 使用remoteMiniInfoService.getShopConfig()
     */
    @ApiModelProperty(value = "商户号")
    @TableField(exist = false)
    @Deprecated
    private String mchId;

    /**
     * 授权状态
     */
    @ApiModelProperty(value = "授权状态    0-未授权 1-授权")
    @TableField("authorizer_flag")
    private Integer authorizerFlag;


    /**
     * 运行状态
     */
    @ApiModelProperty(value = "运行状态  0-未上传代码 1-已上传代码")
    @TableField("run_flag")
    private Integer runFlag;

    /**
     * 授权时间(t_miniauth_info表中的创建时间)
     */
    @ApiModelProperty(value = "授权时间(t_miniauth_info表中的创建时间)")
    @TableField("auth_time")
    @Deprecated
    private LocalDateTime authTime;

    /**
     * 后台备注
     */
    @ApiModelProperty(value = "后台备注")
    @TableField("remark")
    @Deprecated
    private String remark;

    /**
     * 当前模板代码版本id
     */
    @ApiModelProperty(value = "当前模板代码版本id(t_platform_shop_template_detail表id)")
    @TableField("current_version_id")
    private Long currentVersionId;

    /**
     * 当前模板代码版本下的选择上传的
     */
    @ApiModelProperty(value = "当前模板代码版本中的某个小程序版本id(t_platform_shop_template_detail_minis表id)")
    @TableField("template_detail_minis_id")
    private Long templateDetailMinisId;


    /**
     * 当前版本审核记录id
     */
    @ApiModelProperty(value = "当前版本审核记录id")
    @TableField("current_aidit_id")
    private Long currentAiditId;

    /**
     * 审核版本审核记录id
     */
    @ApiModelProperty(value = "审核中版本审核记录表id")
    @TableField("aidit_id")
    private Long aiditId;

    @ApiModelProperty(value = "到期时间")
    @TableField("expiration_time")
    private LocalDateTime expirationTime;

    /**
     * 小程序当前服务套餐id
     */
    @ApiModelProperty(value = "小程序当前服务套餐id")
    @TableField("combo_id")
    @Deprecated
    private Long comboId;

    /**
     * 授权方类型0代表订阅号，1代表由历史老帐号升级后的订阅号，2代表服务号 3代码小程序
     */
    @ApiModelProperty(value = "授权方类型0代表订阅号，1代表由历史老帐号升级后的订阅号，2代表服务号 3代码小程序")
    @TableField("service_type_info")
    private Integer serviceTypeInfo;

    /**
     * 授权方认证类型，-1代表未认证，0代表微信认证，1代表新浪微博认证，2代表腾讯微博认证，3代表已资质认证通过但还未通过名称认证，4代表已资质认证通过、还未通过名称认证，但通过了新浪微博认证，5代表已资质认证通过、还未通过名称认证，但通过了腾讯微博认证
     */
    @ApiModelProperty(
            value = "授权方认证类型，-1代表未认证，0代表微信认证，1代表新浪微博认证，2代表腾讯微博认证，3代表已资质认证通过但还未通过名称认证，4代表已资质认证通过、还未通过名称认证，但通过了新浪微博认证，5代表已资质认证通过、还未通过名称认证，但通过了腾讯微博认证")
    @TableField("verify_type_info")
    private Integer verifyTypeInfo;

    /**
     * 原始ID
     */
    @ApiModelProperty(value = "原始ID")
    @TableField("user_name")
    private String userName;

    /**
     * 公众号绑定的微信账号
     */
    @ApiModelProperty(value = "公众号绑定的微信账号")
    @TableField("alias")
    private String alias;


}
