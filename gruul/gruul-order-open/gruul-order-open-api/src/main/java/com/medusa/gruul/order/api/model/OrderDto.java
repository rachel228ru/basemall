package com.medusa.gruul.order.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 订单
 *
 * @author alan
 * @date 2019/11/5 21:29
 */
@Data
@ApiModel(value = "订单")
public class OrderDto {
    private static final long serialVersionUID = 1L;


    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    private Long id;

    /**
     * 订单状态
     */
    @ApiModelProperty(value = "订单状态")
    private Integer status;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private String userId;

    /**
     * 订单更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private  LocalDateTime updateTime;



}
