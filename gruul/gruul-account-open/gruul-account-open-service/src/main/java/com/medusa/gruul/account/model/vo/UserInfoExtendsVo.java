package com.medusa.gruul.account.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author whh
 * @description
 * @data: 2019/11/19
 */
@Data
@ApiModel(value = "用户扩展信息")
public class UserInfoExtendsVo {

    @ApiModelProperty(value = "拥有积分值")
    private BigDecimal integral;

    @ApiModelProperty(value = "拥有总积分值")
    private BigDecimal integralTotal;

    @ApiModelProperty(value = "连续签到天数")
    private Integer clockNum;

    @ApiModelProperty(value = "最后交易时间")
    private LocalDateTime lastDealTime;

    @ApiModelProperty(value = "是否黑名单用户 0-否 1-是")
    private Integer isBlacklist;

    @ApiModelProperty(value = "消费次数")
    private Integer consumeNum;


    @ApiModelProperty(value = "用户最后一次选择的地理位置经纬度 经纬度,经度在前维度在后逗号分隔")
    private String lastChooseLcation;

    @ApiModelProperty(value = "商铺id")
    private String shopId;

    @ApiModelProperty(value = "最后登录时间")
    private LocalDateTime lastLoginTime;


    @ApiModelProperty(value = "店铺用户id")
    private String shopUserId;

    /**
     * 用户余额
     */
    @ApiModelProperty(value = "用户余额")
    private BigDecimal supplyBonus;

    /**
     * 用户返利余额
     */
    @ApiModelProperty(value = "用户返利余额")
    private BigDecimal rebateBonus;

    /**
     * 冻结余额
     */
    @ApiModelProperty(value = "冻结余额")
    private BigDecimal freezeBonus;
}
