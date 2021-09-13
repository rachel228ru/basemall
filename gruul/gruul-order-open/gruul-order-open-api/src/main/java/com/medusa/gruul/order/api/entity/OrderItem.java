package com.medusa.gruul.order.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.medusa.gruul.common.data.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 订单中所包含的商品
 * </p>
 *
 * @author alan
 * @since 2019-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_order_item")
@ApiModel(value = "OrderItem对象", description = "订单中所包含的商品")
public class OrderItem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商铺ID
     */
    @ApiModelProperty(value = "商铺ID")
    @TableField("shop_id")
    private String shopId;

    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    @TableField("order_id")
    private Long orderId;

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    @TableField("product_id")
    private Long productId;

    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片")
    @TableField("product_pic")
    private String productPic;

    /**
     * 商品名
     */
    @ApiModelProperty(value = "商品名")
    @TableField("product_name")
    private String productName;

    /**
     * 商品编号
     */
    @ApiModelProperty(value = "商品编号")
    @TableField("product_sn")
    private String productSn;

    /**
     * 销售价格 = 商品销售价格
     */
    @ApiModelProperty(value = "销售价格,商品的原价")
    @TableField("product_price")
    private BigDecimal productPrice;

    /**
     * 指导价（划线价）= 单个商品的指导价
     */
    @ApiModelProperty(value = "指导价（划线价），商品的指导价")
    @TableField("product_original_price")
    private BigDecimal productOriginalPrice;

    /**
     * 购买数量
     */
    @ApiModelProperty(value = "购买数量")
    @TableField("product_quantity")
    private Integer productQuantity;

    /**
     * 商品sku编号
     */
    @ApiModelProperty(value = "商品sku编号")
    @TableField("product_sku_id")
    private Long productSkuId;

    /**
     * 商品sku条码
     */
    @ApiModelProperty(value = "商品sku条码")
    @TableField("product_sku_code")
    private String productSkuCode;

    /**
     * 商品促销分解金额 = 满减活动优惠的金额
     */
    @ApiModelProperty(value = "商品促销分解金额")
    @TableField("promotion_amount")
    private BigDecimal promotionAmount;

    /**
     * 优惠券优惠分解金额 = 多个商品的优惠券优惠金额
     */
    @ApiModelProperty(value = "优惠券优惠分解金额")
    @TableField("coupon_amount")
    private BigDecimal couponAmount;


    /**
     * 多个商品经过优惠后的最终金额
     */
    @ApiModelProperty(value = "多个商品经过优惠后的最终金额")
    @TableField("real_amount")
    private BigDecimal realAmount;


    /**
     * 商品的销售属性
     */
    @ApiModelProperty(value = "商品的销售属性")
    @TableField("specs")
    private String specs;


    /**
     * 供应商id
     */
    @ApiModelProperty(value = "供应商id")
    @TableField("provider_id")
    private Long providerId;


    /**
     * 退款金额
     */
    @ApiModelProperty(value = "退款金额")
    @TableField("refund_amount")
    private BigDecimal refundAmount;
}
