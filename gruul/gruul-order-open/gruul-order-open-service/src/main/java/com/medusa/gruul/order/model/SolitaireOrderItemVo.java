package com.medusa.gruul.order.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * The type Solitaire order item vo.
 */
@Data
public class SolitaireOrderItemVo {
    /**
     * 商品名
     */
    @ApiModelProperty(value = "商品名")
    private String productName;

    /**
     * 购买数量
     */
    @ApiModelProperty(value = "购买数量")
    private Integer productQuantity;
}
