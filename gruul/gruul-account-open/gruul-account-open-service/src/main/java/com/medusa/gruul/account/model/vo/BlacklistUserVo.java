package com.medusa.gruul.account.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author whh
 * @description
 * @data: 2019/11/20
 */
@Data
public class BlacklistUserVo {

    @ApiModelProperty(value = "会员卡编号")
    private String memberNumber;

    @ApiModelProperty(value = "会员卡余额")
    private BigDecimal supplyBonus;

    @ApiModelProperty(value = "会员注册时间")
    private LocalDateTime memberRegisterTime;

    @ApiModelProperty(value = "成为会员天数")
    private Integer becomeMemberDayNumber;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "用户名称")
    private String nikeName;

    @ApiModelProperty(value = "头像url")
    private String avatarUrl;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "消费次数")
    private Integer consumeNum;

    @ApiModelProperty(value = "交易总额")
    private BigDecimal consumeTotleMoney;

    @ApiModelProperty(value = "可用积分")
    private BigDecimal integral;

    @ApiModelProperty(value = "当前限制  1-限制下单,2-限制评论")
    private List<Integer> blacklistType;

}
