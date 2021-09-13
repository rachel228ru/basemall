package com.medusa.gruul.afs.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * The type Negotiate history dto.
 *
 * @author alan
 * @description: 获取订单售后协商历史
 * @date 2020 /8/5 21:54
 */
@Data
@ApiModel(value = "获取订单售后协商历史")
public class NegotiateHistoryDto {

    @ApiModelProperty(value = "需要查询的订单")
    private Long orderId;

    @ApiModelProperty(value = "用户类型 1.用户不传查询所有")
    private String type;
}
