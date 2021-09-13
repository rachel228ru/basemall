package com.medusa.gruul.order.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * The type Manage search logistics order dto.
 * <p>
 * 快递订单后台订单查询条件
 */
@Data
@ApiModel(value = "快递订单后台订单查询条件")
public class ManageSearchLogisticsOrderDto implements Serializable {
    private static final long serialVersionUID = -3263921252635611410L;

    @ApiModelProperty(value = "订单ID列表")
    private String orderIds;


}
