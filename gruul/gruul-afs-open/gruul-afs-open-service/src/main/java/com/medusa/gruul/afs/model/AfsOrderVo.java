package com.medusa.gruul.afs.model;

import com.medusa.gruul.afs.api.entity.AfsOrder;
import com.medusa.gruul.afs.api.entity.AfsOrderItem;
import com.medusa.gruul.order.api.model.OrderVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * The type Afs order vo.
 *
 * @author alan
 * @description: 用户申请售后的参数
 * @date 2020 /8/5 21:54
 */
@Data
@ApiModel(value = "用户申请售后的参数")
public class AfsOrderVo extends AfsOrder {


    @ApiModelProperty(value = "商品详情")
    private AfsOrderItem item;

    @ApiModelProperty(value = "原始订单信息")
    private OrderVo originalOrder;

}
