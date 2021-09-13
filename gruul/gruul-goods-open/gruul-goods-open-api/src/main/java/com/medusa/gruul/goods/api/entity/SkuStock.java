package com.medusa.gruul.goods.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.medusa.gruul.common.data.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * <p>
 * sku的库存
 * </p>
 *
 * @author alan
 * @since 2019-09-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_sku_stock")
@ApiModel(value = "SkuStock对象", description = "sku的库存")
public class SkuStock extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 本店店铺id
     */
    @ApiModelProperty(value = "本店店铺id")
    @TableField("shop_id")
    private String shopId;

    /**
     * 版本号
     */
    @Version
    @ApiModelProperty(value = "版本号")
    @TableField("version")
    private Long version;
    /**
     * 产品id
     */
    @ApiModelProperty(value = "产品id")
    @TableField("product_id")
    private Long productId;

    /**
     * sku编码
     */
    @ApiModelProperty(value = "sku编码")
    @TableField("sku_code")
    private String skuCode;

    /**
     * 商品规格
     */
    @NotBlank
    @Size(max = 64)
    @ApiModelProperty(value = "商品规格")
    @TableField("specs")
    private String specs;

    /**
     * 商品sku重量
     */
    @NotBlank
    @ApiModelProperty(value = "商品sku重量")
    @TableField("weight")
    private BigDecimal weight;

    /**
     * 展示图片
     */
    @NotBlank
    @Size(max = 255)
    @ApiModelProperty(value = "展示图片")
    @TableField("pic")
    private String pic;

    /**
     * 实售价
     */
    @NotNull
    @ApiModelProperty(value = "实售价")
    @TableField("price")
    private BigDecimal price;

    /**
     * 指导价（划线价）
     */
    @NotNull
    @ApiModelProperty(value = "指导价（划线价）")
    @TableField("original_price")
    private BigDecimal originalPrice;

    /**
     * 库存
     */
    @NotNull
    @ApiModelProperty(value = "库存")
    @TableField("stock")
    private Integer stock;

    /**
     * 预警库存
     */
    @NotNull
    @ApiModelProperty(value = "预警库存")
    @TableField("low_stock")
    private Integer lowStock;

    /**
     * 销量
     */
    @ApiModelProperty(value = "销量")
    @TableField("sale")
    private Integer sale;

    /**
     * 限购数量
     */
    @ApiModelProperty(value = "限购数量")
    @TableField("per_limit")
    private Integer perLimit;


}