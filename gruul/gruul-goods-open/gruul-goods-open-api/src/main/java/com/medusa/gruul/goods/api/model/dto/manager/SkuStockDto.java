package com.medusa.gruul.goods.api.model.dto.manager;

import cn.hutool.core.bean.BeanUtil;
import com.medusa.gruul.goods.api.entity.SkuStock;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 商品sku
 * </p>
 *
 * @author lcysike
 * @since 2019-10-06
 */
@Data
@ApiModel(value = "新增或修改商品sku DTO")
public class SkuStockDto implements Serializable {

    private Long id;

    @ApiModelProperty(value = "版本号")
    private Long version;

    @ApiModelProperty(value = "产品id")
    private Long productId;

    @NotNull
    @ApiModelProperty(value = "sku编码")
    private String skuCode;

    @NotBlank
    @ApiModelProperty(value = "商品规格")
    private String specs;

    @ApiModelProperty(value = "商品sku重量")
    private BigDecimal weight;

    @NotBlank
    @ApiModelProperty(value = "展示图片")
    private String pic;

    @NotNull
    @ApiModelProperty(value = "实售价")
    private BigDecimal price;

    @NotNull
    @ApiModelProperty(value = "指导价（划线价）")
    private BigDecimal originalPrice;

    @NotNull
    @ApiModelProperty(value = "库存")
    private Integer stock;

    @NotNull
    @ApiModelProperty(value = "预警库存")
    private Integer lowStock;

    @ApiModelProperty(value = "销量")
    private Integer sale;

    @ApiModelProperty(value = "限购数量")
    private Integer perLimit;

    public SkuStock coverSkuStock() {
        SkuStock skuStock = new SkuStock();
        BeanUtil.copyProperties(this, skuStock);
        return skuStock;
    }
}