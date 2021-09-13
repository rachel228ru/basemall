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
@ApiModel(value = "用户列表")
public class UserListVo {

    @ApiModelProperty(value = "会员卡编号")
    private String memberNumber;

    @ApiModelProperty(value = "会员卡余额")
    private BigDecimal supplyBonus;

    @ApiModelProperty(value = "会员卡返利余额")
    private BigDecimal rebateBonus;

    @ApiModelProperty(value = "会员注册时间")
    private LocalDateTime memberRegisterTime;

    @ApiModelProperty(value = "成为会员天数")
    private Long becomeMemberDayNumber;

    @ApiModelProperty(value = "店铺用户id")
    private String userId;

    @ApiModelProperty(value = "用户名称")
    private String nikeName;

    @ApiModelProperty(value = "头像url")
    private String avatarUrl;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "最后交易时间")
    private LocalDateTime orderLastDealTime;

    @ApiModelProperty(value = "消费次数")
    private Integer consumeNum;

    @ApiModelProperty(value = "交易总额")
    private BigDecimal consumeTotleMoney;

    @ApiModelProperty(value = "可用积分")
    private BigDecimal integral;

    @ApiModelProperty(value = "用户身份类型 0-普通用户 ")
    private Integer communityType;

    @ApiModelProperty(value = "用户拥护的标签")
    private List<UserTagVo> userTagVos;

    @ApiModelProperty(value = "首次登陆小程序时间")
    private LocalDateTime firstLoginTime;



    @ApiModelProperty(value = "是否是会员：1是，2否")
    private Integer state;
}
