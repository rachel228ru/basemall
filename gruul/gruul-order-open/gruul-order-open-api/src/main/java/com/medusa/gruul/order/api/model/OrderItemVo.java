package com.medusa.gruul.order.api.model;

import com.medusa.gruul.afs.api.entity.AfsOrder;
import com.medusa.gruul.order.api.entity.OrderItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单详情
 *
 * @author alan
 * @date 2019/11/5 21:29
 */
@Data
@ApiModel(value = "订单详情")
public class OrderItemVo extends OrderItem {
    private static final long serialVersionUID = 1L;

    /**
     * 售后信息
     */
    @ApiModelProperty(value = "售后信息")
    private AfsOrder afs;
}
