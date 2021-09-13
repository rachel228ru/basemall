package com.medusa.gruul.platform.model.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


/**
 * 小程序基本信息接口
 *
 * @author whh
 * @description
 * @data: 2020/1/16
 */
@Data
public class BaseInfoVo {

    @ApiModelProperty(value = "小程序名称||公众号名称")
    private String miniName;
    @ApiModelProperty(value = "小程序头像||公众号头像")
    private String logo;
    @ApiModelProperty(value = "小程序码||公众号二维码")
    private String qrcode;
    @ApiModelProperty(value = "小程序介绍|公众号介绍")
    private String signature;
    @ApiModelProperty(value = "主体信息")
    private String principalName;
    @ApiModelProperty(value = "服务类目")
    private String serviceClass;
    @ApiModelProperty(value = "公众号绑定的微信账号")
    private String alias;
    @ApiModelProperty(value = "小程序或公众号appid")
    private String appid;

    @ApiModelProperty(value = "授权方类型0代表订阅号，1代表由历史老帐号升级后的订阅号，2代表服务号 3代码小程序")
    private Integer serviceTypeInfo;
    @ApiModelProperty(
            value = "授权方认证类型，-1代表未认证，0代表微信认证，1代表新浪微博认证，2代表腾讯微博认证，3代表已资质认证通过但还未通过名称认证，4代表已资质认证通过、还未通过名称认证，但通过了新浪微博认证，5代表已资质认证通过、还未通过名称认证，但通过了腾讯微博认证")
    private Integer verifyTypeInfo;
    @ApiModelProperty(value = "当前版本号名称")
    private String currentVersionNumName;
    @ApiModelProperty(value = "当前版本号发布时间")
    private LocalDateTime currentVersionSendTime;
    @ApiModelProperty(value = "审核状态 0审核通过,1审核失败，2审核中")
    private Integer auditStatus;
    @ApiModelProperty(value = "审核中的版本名称")
    private String auditingVersionNumName;
    @ApiModelProperty(value = "审核提交时间")
    private LocalDateTime auditingVersionSummitTime;
    @ApiModelProperty(value = "审核失败原因")
    private String auditingComeToNothingReason;
    @ApiModelProperty(value = "审核结束时间")
    private LocalDateTime auditingVersionEndTime;
    @ApiModelProperty(value = "审核店铺模版详情小程序版本子表id")
    private Long auditingTemplateDetailMinisId;
    @ApiModelProperty(value = "最新的版本号名称")
    private String versionUpdateNumName;
    @ApiModelProperty(value = "最新版本号更新时间")
    private LocalDateTime versionUpdateTime;
    @ApiModelProperty(value = "子版本号列表")
    private List<MiniCodeVersionVo> codeVersionVos;
    @ApiModelProperty(value = "小程序状态授权  true授权  false未授权")
    private Boolean authorizerFlag;
    @ApiModelProperty(value = "运行状态 false-已关闭  true-运行中")
    private Boolean runFlag;
    @ApiModelProperty(value = "授权信息")
    private List<String> authInfo;
}
