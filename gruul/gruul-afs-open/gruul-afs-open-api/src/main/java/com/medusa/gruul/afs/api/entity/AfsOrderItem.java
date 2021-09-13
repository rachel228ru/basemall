package com.medusa.gruul.afs.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.medusa.gruul.common.data.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 申请的详情
 * </p>
 *
 * @author alan
 * @since 2020 -08-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_afs_order_item")
@ApiModel(value = "AfsOrderItem对象", description = "详情")
public class AfsOrderItem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @TableId("id")
    private Long id;

    /**
     * 商铺ID
     */
    @ApiModelProperty(value = "商铺ID")
    @TableField("shop_id")
    private String shopId;

    /**
     * 售后申请单ID
     */
    @ApiModelProperty(value = "售后申请单ID")
    @TableField("afs_id")
    private Long afsId;

    /**
     * 申请售后的订单ID
     */
    @ApiModelProperty(value = "申请售后的订单ID")
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
     * 商品规格
     */
    @TableField("specs")
    @ApiModelProperty(value = "商品规格")
    private String specs;

    /**
     * 销售价格
     */
    @ApiModelProperty(value = "销售价格")
    @TableField("product_price")
    private BigDecimal productPrice;

    /**
     * 退款金额
     */
    @ApiModelProperty(value = "退款金额")
    @TableField("refund_amount")
    private BigDecimal refundAmount;


}
