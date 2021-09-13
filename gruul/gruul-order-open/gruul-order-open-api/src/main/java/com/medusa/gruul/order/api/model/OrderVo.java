package com.medusa.gruul.order.api.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.medusa.gruul.order.api.entity.Order;
import com.medusa.gruul.order.api.entity.OrderDelivery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单详情
 *
 * @author alan
 * @date 2019/11/5 21:29
 */
@Data
@ApiModel(value = "订单详情")
public class OrderVo extends Order {
    private static final long serialVersionUID = 1L;


    /**
     * 订单单个商品详情
     */
    @ApiModelProperty(value = "订单单个商品详情")
    private List<OrderItemVo> orderItemList;

    /**
     * 订单物流详情
     */
    @ApiModelProperty(value = "订单物流详情")
    private OrderDelivery orderDelivery;

    /**
     * 过期时间 前端倒计时用
     */
    @ApiModelProperty(value = "过期时间")
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime expireDate;


    /**
     * 商品总数量
     */
    @ApiModelProperty(value = "商品总数量")
    @TableField(exist = false)
    private Integer productTotalQuantity;

    /**
     * 退款数量
     */
    @ApiModelProperty(value = "退款数量")
    @TableField(exist = false)
    private Integer refundQuantity;
}
