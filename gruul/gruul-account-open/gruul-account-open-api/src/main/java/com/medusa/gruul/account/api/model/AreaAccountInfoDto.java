package com.medusa.gruul.account.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author whh
 * @description
 * @data: 2020/5/11
 */
@Data
public class AreaAccountInfoDto {

    @ApiModelProperty(value = "用户店铺id")
    private String shopUserId;

    @ApiModelProperty(value = "用户名称")
    private String nikeName;

    @ApiModelProperty(value = "头像url")
    private String avatarUrl;

    @ApiModelProperty(value = "首次登陆小程序时间")
    private LocalDateTime firstLoginTime;

    @ApiModelProperty(value = "消费次数")
    private Integer consumeNum;

    @ApiModelProperty(value = "交易总额")
    private BigDecimal consumeTotleMoney;

    @ApiModelProperty(value = "最后交易时间")
    private LocalDateTime lastDealTime;



}
