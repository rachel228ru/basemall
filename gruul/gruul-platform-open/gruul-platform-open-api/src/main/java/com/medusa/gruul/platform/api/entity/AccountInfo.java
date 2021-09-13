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

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 平台与租户平台用户表
 * </p>
 *
 * @author whh
 * @since 2019-09-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_platform_account_info")
@ApiModel(value = "AccountInfo对象", description = "平台与租户平台用户表")
public class AccountInfo extends BaseNoTenantEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 账号状态  0-正常  1-禁用
     */
    @ApiModelProperty(value = "账号状态")
    @TableField("forbid_status")
    private Integer forbidStatus;


    /**
     * 小程序账号唯一id
     */
    @ApiModelProperty(value = "小程序唯一id")
    @TableField("bind_mini_id")
    private String bindMiniId;

    /**
     * 小程序店铺唯一id
     */
    @ApiModelProperty(value = "小程序店铺唯一id")
    @TableField("bind_mini_shop_id")
    private String bindMiniShopId;

    /**
     * 所属主体账号id,也就是平台管理的用户id
     */
    @ApiModelProperty(value = "所属主体账号id,也就是平台管理的用户id")
    @TableField("subject_id")
    private Long subjectId;

    /**
     * 账号类型  0-商户账号
     */
    @ApiModelProperty(value = "账号类型")
    @TableField("account_type")
    private Integer accountType;


    /**
     * 不加密的密码-临时用
     */
    @ApiModelProperty(value = "不加密的密码")
    @TableField("password")
    private String password;

    /**
     * 账号登录密码-加密
     */
    @ApiModelProperty(value = "账号登录密码-加密")
    @TableField("passwd")
    private String passwd;

    /**
     * 盐值
     */
    @ApiModelProperty(value = "盐值")
    @TableField("salt")
    private String salt;

    /**
     * 所在城市
     */
    @ApiModelProperty(value = "微信获得所在城市")
    @TableField("city")
    private String city;

    /**
     * 所用的语言 en 英文 - zh_CN 简体中文 -zh_TW 繁体中文
     */
    @ApiModelProperty(value = "所用的语言 en 英文 - zh_CN 简体中文 -zh_TW 繁体中文")
    @TableField("language")
    private String language;

    /**
     * 最后登录时间
     */
    @ApiModelProperty(value = "最后登录时间")
    @TableField("last_login_time")
    private LocalDateTime lastLoginTime;

    /**
     * 登录token-微信网页应用
     */
    @ApiModelProperty(value = "登录token-微信网页应用")
    @TableField("access_token")
    private String accessToken;

    /**
     * 刷新token-微信网页应用 refresh_token拥有较长的有效期（30天），当refresh_token失效的后，需要用户重新授权。
     */
    @ApiModelProperty(value = "刷新token-微信网页应用 refresh_token拥有较长的有效期（30天），当refresh_token失效的后，需要用户重新授权。")
    @TableField("refresh_token")
    private String refreshToken;

    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户名称")
    @TableField("nike_name")
    private String nikeName;

    /**
     * 头像url
     */
    @ApiModelProperty(value = "头像url")
    @TableField("avatar_url")
    private String avatarUrl;

    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码")
    @TableField("phone")
    private String phone;

    /**
     * 电子发票接收邮箱
     */
    @ApiModelProperty(value = "电子发票接收邮箱")
    @TableField("email")
    private String email;

    /**
     * 性别 0：未知、1：男、2：女
     */
    @ApiModelProperty(value = "性别 0：未知、1：男、2：女")
    @TableField("gender")
    private Integer gender;

    /**
     * 第三方平台openid
     */
    @ApiModelProperty(value = "第三方平台openid")
    @TableField("open_id")
    private String openId;

    /**
     * 第三方平台unionid,针对一个微信开放平台帐号下的应用
     */
    @ApiModelProperty(value = "第三方平台unionid,针对一个微信开放平台帐号下的应用")
    @TableField("union_id")
    private String unionId;

    /**
     * 省份
     */
    @ApiModelProperty(value = "微信返回省份")
    @TableField("province")
    private String province;

    /**
     * 国家
     */
    @ApiModelProperty(value = "微信返回国家")
    @TableField("country")
    private String country;

    /**
     * 用户特权信息，json数组，如微信沃卡用户为（chinaunicom）
     */
    @ApiModelProperty(value = "用户特权信息，json数组，如微信沃卡用户为（chinaunicom）")
    @TableField("privilege")
    private String privilege;

    /**
     * refresh_token到期时间
     */
    @ApiModelProperty(value = "refresh_token到期时间")
    @TableField("ref_expires_time")
    private LocalDateTime refExpiresTime;

    /**
     * access_token到期时间
     */
    @ApiModelProperty(value = "access_token到期时间")
    @TableField("access_expires_time")
    private LocalDateTime accessExpiresTime;

    @ApiModelProperty(value = "注册时填写地区")
    @TableField("region")
    private String region;

    @ApiModelProperty(value = "注册时填写住址")
    @TableField("address")
    private String address;

    @ApiModelProperty(value = "最后一次进入的店铺id")
    @TableField("last_login_shop_id")
    private Long lastLoginShopId;

    @ApiModelProperty(value = "用户余额")
    @TableField("balance")
    private BigDecimal balance;

    @ApiModelProperty(value = "所属代理")
    @TableField("agent_id")
    private Long agentId;

    @ApiModelProperty(value = "备注")
    @TableField("comment_text")
    private String commentText;

    @ApiModelProperty(value = "用户的代理账号id")
    @TableField("me_agent_id")
    private Long meAgentId;

}
