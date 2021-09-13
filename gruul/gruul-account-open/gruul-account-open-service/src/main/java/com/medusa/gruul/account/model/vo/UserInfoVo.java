package com.medusa.gruul.account.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author whh
 * @description
 * @data: 2019/11/19
 */
@Data
@ApiModel(value = "用户基本信息")
public class UserInfoVo {

    @ApiModelProperty(value = "用户店铺id")
    private String shopUserId;

    @ApiModelProperty(value = "所在国家")
    private String country;

    @ApiModelProperty(value = "所在省份")
    private String province;

    @ApiModelProperty(value = "所在城市")
    private String city;

    @ApiModelProperty(value = "所用的语言 en 英文 - zh_CN 简体中文 -zh_TW 繁体中文 ")
    private String language;

    @ApiModelProperty(value = "用户名称")
    private String nikeName;

    @ApiModelProperty(value = "头像url")
    private String avatarUrl;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "性别 0：未知、1：男、2：女")
    private Integer gender;

    @ApiModelProperty(value = "首次登陆小程序时间")
    private LocalDateTime firstLoginTime;

    @ApiModelProperty(value = "是否授权过小程序 false 未授权 turn授权过")
    private Boolean whetherAuthorization;

    @ApiModelProperty(value = "数据级别为2时才返回,用户扩展信息")
    private UserInfoExtendsVo infoExtends;


    @ApiModelProperty(value = "数据级别为3时才返回,用户限制类型")
    private UserBlacklistAstrictVo astrictVo;
}
