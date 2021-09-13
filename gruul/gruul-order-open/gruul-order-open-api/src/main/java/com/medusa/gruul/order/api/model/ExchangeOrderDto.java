package com.medusa.gruul.order.api.model;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 创建换货单的参数
 *
 * @author alan
 * @date 2020/8/23 13:02
 */
@Data
@ApiModel(value = "创建换货单的参数")
public class ExchangeOrderDto {
    /**
     * 售后申请单ID
     */
    @ApiModelProperty(value = "售后申请单ID")
    @TableField("afs_id")
    private Long afsId;

    /**
     * 原始订单
     */
    @ApiModelProperty(value = "原始订单")
    @TableField("order_id")
    private Long orderId;

    /**
     * 商品sku编号
     */
    @ApiModelProperty(value = "商品sku编号")
    @TableField("product_sku_id")
    private Long productSkuId;

    /**
     * 商品数量
     */
    @ApiModelProperty(value = "商品数量")
    @TableField("product_quantity")
    private Integer productQuantity;


}
