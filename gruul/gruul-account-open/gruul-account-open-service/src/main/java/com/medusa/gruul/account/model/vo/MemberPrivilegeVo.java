package com.medusa.gruul.account.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @Description 会员权益返回集合
 * @Author zhaokw
 * @Date 21:19 2020\5\15 0015
 **/
@Data
@ApiModel(value = "会员权益返回集合")
@Accessors(chain = true)
public class MemberPrivilegeVo {

    @ApiModelProperty(value = "权益id")
    private Long id;

    @ApiModelProperty(value = "权益名称")
    private String privilegeName;

    @ApiModelProperty(value = "是否选中,0表示未选中，1表示选中")
    private Integer isSelected;

    @ApiModelProperty(value = "特权类型 0-自定义权益 1-商品权益 2-包邮权益 3-积分倍数权益 4-专属客服权益 5-优先发货 6-急速售后")
    private String pType;

    @ApiModelProperty(value = "积分加倍或商品折扣值 商品折扣：10-99之间的值带%，积分加倍：2-50之间的值")
    private BigDecimal value;

    /**
     * 权益图标
     */
    @ApiModelProperty(value = "权益图标")
    private String icon;

    /**
     * 权益说明
     */
    @ApiModelProperty(value = "权益说明")
    private String illustrate;

}
