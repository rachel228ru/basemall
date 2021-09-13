package com.medusa.gruul.order.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户订单概况
 *
 * @author alan
 * @date 2019/11/13 20:20
 */
@Data
public class OrderOverviewVo implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 待付款订单
     */
    @ApiModelProperty(value = "待付款订单")
    private Long waitForPay;

    /**
     * 配送中订单
     */
    @ApiModelProperty(value = "配送中订单")
    private Long shipped;

    /**
     * 待提货订单
     */
    @ApiModelProperty(value = "待提货订单")
    private Long waitForPickup;

    /**
     * 待发货订单
     */
    @ApiModelProperty(value = "待发货订单")
    private Long withDelivery;

    /**
     *
     */
    @ApiModelProperty(value = "售后订单")
    private Long afsOrder;
}
