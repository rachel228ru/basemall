package com.medusa.gruul.order.model;

import com.medusa.gruul.afs.api.entity.AfsOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The type Simple order item vo.
 * <p>
 * 简单的单个商品返回结果
 *
 * @author alan
 * @date 2019 /11/5 21:29
 */
@Data
@ApiModel(value = "简单的单个商品返回结果")
public class SimpleOrderItemVo implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 订单详情id
     */
    @ApiModelProperty(value = "订单详情id", example = "1")
    private Long id;


    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id", example = "1")
    private Long productId;

    /**
     * 商品sku编号
     */
    @ApiModelProperty(value = "商品sku编号", example = "1")
    private Long productSkuId;


    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片")
    private String productPic;

    /**
     * 商品名
     */
    @ApiModelProperty(value = "商品名")
    private String productName;

    /**
     * 销售价格
     */
    @ApiModelProperty(value = "销售价格")
    private BigDecimal productPrice;

    /**
     * 实际价格
     */
    @ApiModelProperty(value = "实际价格")
    private BigDecimal realAmount;

    /**
     * 指导价（划线价）
     */
    @ApiModelProperty(value = "指导价（划线价）")
    private BigDecimal productOriginalPrice;

    /**
     * 购买数量
     */
    @ApiModelProperty(value = "购买数量")
    private Integer productQuantity;


    /**
     * 商品规格
     */
    @ApiModelProperty(value = "商品规格")
    private String specs;


    /**
     * 订单ID
     */
    @ApiModelProperty(value = "订单ID")
    private Long orderId;

    /**
     * 售后信息
     */
    @ApiModelProperty(value = "售后信息")
    private AfsOrder afs;

}
