package com.medusa.gruul.order.model;

import com.medusa.gruul.common.core.param.QueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * The type Api search order dto.
 * <p>
 * 小程序端订单查询条件
 *
 * @author alan
 * @date 2019 /11/10 9:21
 */
@Data
@ApiModel(value = "小程序端订单查询条件")
public class ApiSearchOrderDto extends QueryParam {

    @NotNull
    @ApiModelProperty(value = "订单状态 -1：所有订单, 0.待付款（待买家付款）, 1.待发货（买家已付款）, 2.配送中（卖家已发货）, 3.待提货（商家直配已到达提货点或物流订单已发货）, 4" +
            ".已完成（用户已经签收）, 5.待评价, 7.已关闭")
    private Integer orderStatus;
}
