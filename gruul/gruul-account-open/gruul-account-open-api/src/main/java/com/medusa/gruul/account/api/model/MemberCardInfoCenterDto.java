package com.medusa.gruul.account.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description 当前用户会员卡信息返回值
 * @Author zhaokw
 * @Date 23:43 2020\6\9 0009
 **/
@Data
@ApiModel(value = "当前用户会员卡信息返回值")
@Accessors(chain = true)
public class MemberCardInfoCenterDto {

    /**
     * 会员id
     */
    @ApiModelProperty(value = "会员id ")
    private Long id;

    /**
     * 会员卡编号
     */
    @ApiModelProperty(value = "会员卡编号")
    private String memberNumber;

    /**
     * 会员卡注册状态 0未注册,1已注册 2-会员到期
     */
    @ApiModelProperty(value = "会员卡注册状态 0未注册,1已注册  2-会员到期")
    private Integer state;

    /**
     * 会员卡Id
     */
    @ApiModelProperty(value = "会员卡Id")
    private Long memberCardInfoId;

    /**
     * 会员注册时间
     */
    @ApiModelProperty(value = "会员注册时间")
    private LocalDateTime memberRegisterTime;

    /**
     * 会员到期时间
     */
    @ApiModelProperty(value = "会员到期时间")
    private LocalDateTime memberExpireTime;

    @ApiModelProperty(value = "会员卡名称")
    private String title;

    @ApiModelProperty(value = "会员卡等级代码")
    private String rankCode;

    @ApiModelProperty(value = "会员卡等级名称")
    private String rankName;



    /**
     * 会员卡开卡方式:0-自动发卡 1-付费购买 2-积分兑换
     */
    @ApiModelProperty(value = "会员卡开卡方式:0-自动发卡 1-付费购买 2-积分兑换")
    private Integer openType;

    /**
     * 开通所需值(付费金额或所需积分),累计消费满多少金额
     */
    @ApiModelProperty(value = "开通所需值(付费金额或所需积分),累计消费满多少金额")
    private BigDecimal openValue;

    /**
     * 使用期限 按天计算 0表示不限制
     */
    @ApiModelProperty(value = "使用期限 按天计算  0表示不限制")
    private Long useInDate;

    /**
     * 用户余额
     */
    @ApiModelProperty(value = "用户余额")
    private BigDecimal supplyBonus;

}
