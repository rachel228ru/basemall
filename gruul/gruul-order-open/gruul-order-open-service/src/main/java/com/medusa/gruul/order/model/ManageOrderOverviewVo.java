package com.medusa.gruul.order.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * The type Manage order overview vo.
 * <p>
 * 订单概况
 *
 * @author alan
 * @date 2019 /11/13 20:20
 */
@Data
@ApiModel("订单概况")
public class ManageOrderOverviewVo implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 待付款订单
     */
    @ApiModelProperty(value = "待付款订单")
    private Integer waitForPay;

    /**
     * 待发货订单
     */
    @ApiModelProperty(value = "待发货订单")
    private Integer waitForSend;

    /**
     * 待签收订单
     */
    @ApiModelProperty(value = "待签收订单")
    private Integer shipped;

    /**
     * 待提货订单
     */
    @ApiModelProperty(value = "待提货订单")
    private Integer waitForPickup;
}
