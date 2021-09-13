package com.medusa.gruul.platform.model.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * @author whh
 * @description
 * @data: 2020/3/7
 */
@Data
public class MerchanListVo {

    /**
     * 商户id
     */
    @ApiModelProperty(value = "商户id")
    private Long id;

    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户名称")
    private String nikeName;

    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码")
    private String phone;


    /**
     * 头像url
     */
    @ApiModelProperty(value = "头像url")
    private String avatarUrl;

    /**
     * 账号类型  0-商户账号
     */
    @ApiModelProperty(value = "账号类型")
    private String accountType;

    @ApiModelProperty(value = "地区")
    private String region;

    @ApiModelProperty(value = "住址")
    private String address;

    @ApiModelProperty(value = "最后登录时间")
    private LocalDateTime lastLoginTime;


    @ApiModelProperty(value = "店铺数量")
    private Integer shopNumber;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "账号状态  0-正常  1-禁用")
    private Integer forbidStatus;

    @ApiModelProperty(value = "邀请码")
    private String inviteCode;

    @ApiModelProperty(value = "所属代理id(渠道商或代理id)")
    private Long agentId;

    @ApiModelProperty(value = "代理名称")
    private String agentName;

    @ApiModelProperty(value = "代理")
    private String agentPhone;

    @ApiModelProperty(value = "渠道商名称")
    private String channelName;

    @ApiModelProperty(value = "渠道商账号")
    private String channelPhone;

}
