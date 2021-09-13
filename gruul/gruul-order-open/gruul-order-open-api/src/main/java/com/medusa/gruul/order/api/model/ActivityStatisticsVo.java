package com.medusa.gruul.order.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author alan
 */
@Data
public class ActivityStatisticsVo {
    /**
     * 参与人数
     */
    @ApiModelProperty(value = "参与人数")
    private Integer peopleNum;


    /**
     * 支付订单数
     */
    @ApiModelProperty(value = "支付订单数")
    private Integer payOrder;


    /**
     * 金额总数
     */
    @ApiModelProperty(value = "金额总数")
    private BigDecimal gaapTotal;


    /**
     * 活动ID
     */
    @ApiModelProperty(value = "活动ID")
    private Long activityId;
}
