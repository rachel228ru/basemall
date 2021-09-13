package com.medusa.gruul.account.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author whh
 * @description
 * @data: 2019/12/3
 */
@Data
public class BlacklistUserDto {

    @ApiModelProperty(value = "会员卡编号")
    private String memberNumber;

    @ApiModelProperty(value = "会员卡余额")
    private BigDecimal supplyBonus;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "店铺用户id")
    private String shopUserId;

    @ApiModelProperty(value = "用户名称")
    private String nikeName;

    @ApiModelProperty(value = "头像url")
    private String avatarUrl;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "会员注册时间")
    private LocalDateTime memberRegisterTime;

    @ApiModelProperty(value = "首次登陆小程序时间")
    private LocalDateTime firstLoginTime;

    @ApiModelProperty(value = "消费次数")
    private Integer consumeNum;

    @ApiModelProperty(value = "交易总额")
    private BigDecimal consumeTotleMoney;

    @ApiModelProperty(value = "可用积分")
    private BigDecimal integral;
}
