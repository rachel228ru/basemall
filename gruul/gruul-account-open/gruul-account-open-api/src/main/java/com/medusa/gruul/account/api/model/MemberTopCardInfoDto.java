package com.medusa.gruul.account.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description 当前租户最高会员卡信息返回值
 * @Author zhaokw
 * @Date 23:43 2020\7\2 0009
 **/
@Data
@ApiModel(value = "当前租户最高会员卡信息返回值")
@Accessors(chain = true)
public class MemberTopCardInfoDto {

    @ApiModelProperty(value = "会员卡id")
    private Long id;

    /**
     * 会员卡等级代码
     */
    @ApiModelProperty(value = "会员卡等级代码")
    private String rankCode;

    /**
     * 会员卡名称
     */
    @ApiModelProperty(value = "会员卡名称")
    private String rankName;

    /**
     * 会员卡图片
     */
    @ApiModelProperty(value = "会员卡图片")
    private String backGroundPicUrl;

    /**
     * 会员卡标题
     */
    @ApiModelProperty(value = "会员卡标题")
    private String title;

    /**
     * 开通所需值(付费金额或所需积分)
     */
    @ApiModelProperty(value = "开通所需值(付费金额或所需积分)")
    private BigDecimal openValue;

    /**
     * 会员卡介绍
     */
    @ApiModelProperty(value = "会员卡介绍")
    private String introduce;

    /**
     * 会员卡开卡方式:0-自动发卡 1-付费购买 2-积分兑换
     */
    @ApiModelProperty(value = "会员卡开卡方式:0-自动发卡 1-付费购买 2-积分兑换")
    private Integer openType;

    /**
     * 使用期限 按天计算 0表示不限制
     */
    @ApiModelProperty(value = "使用期限 按天计算  0表示不限制")
    private Long useInDate;

    /**
     * 开卡条件1 累计消费满多少元 0元表示不限制
     */
    @ApiModelProperty(value = "开卡条件1 累计消费满多少元  0元表示不限制")
    private BigDecimal consumeCondition;

    /**
     * 开卡条件2 是否需要购买指定商品,0-不需要 1-需要(查询关联子表)
     */
    @ApiModelProperty(value = "开卡条件2 是否需要购买指定商品,0-不需要 1-需要(查询关联子表)")
    private Integer buyGoods;

    /**
     * 会员卡是否开启 0-关闭 1-开启
     */
    @ApiModelProperty(value = "会员卡是否开启 0-关闭 1-开启")
    private Integer isOpen;

    /**
     * 会员卡风格 1-系统风格 2-自定义风格
     */
    @ApiModelProperty(value = "会员卡风格 1-系统风格 2-自定义风格")
    private Integer style;


}
