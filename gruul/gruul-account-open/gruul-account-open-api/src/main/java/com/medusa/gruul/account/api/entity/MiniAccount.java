package com.medusa.gruul.account.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.medusa.gruul.common.data.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 小程序用户表
 * </p>
 *
 * @author whh
 * @since 2019-11-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_mini_account")
@ApiModel(value = "MiniAccount对象", description = "小程序用户表")
public class MiniAccount extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id ")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private String userId;

    /**
     * 所在国家
     */
    @ApiModelProperty(value = "所在国家")
    @TableField("country")
    private String country;

    /**
     * 所在省份
     */
    @ApiModelProperty(value = "所在省份")
    @TableField("province")
    private String province;

    /**
     * 所在城市
     */
    @ApiModelProperty(value = "所在城市")
    @TableField("city")
    private String city;

    /**
     * 所用的语言 en 英文 - zh_CN 简体中文 -zh_TW 繁体中文
     */
    @ApiModelProperty(value = "所用的语言 en 英文 - zh_CN 简体中文 -zh_TW 繁体中文 ")
    @TableField("language")
    private String language;

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
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    @TableField("email")
    private String email;

    /**
     * 性别 0：未知、1：男、2：女
     */
    @ApiModelProperty(value = "性别 0：未知、1：男、2：女")
    @TableField("gender")
    private Integer gender;

    /**
     * 首次登陆小程序时间
     */
    @ApiModelProperty(value = "首次登陆小程序时间")
    @TableField("first_login_time")
    private LocalDateTime firstLoginTime;


    /**
     * 是否授权过小程序
     */
    @ApiModelProperty(value = "是否授权过小程序 false 未授权  turn授权过")
    @TableField("whether_authorization")
    private Boolean whetherAuthorization;

}
