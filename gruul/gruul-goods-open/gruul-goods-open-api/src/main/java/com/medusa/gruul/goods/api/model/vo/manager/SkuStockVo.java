package com.medusa.gruul.goods.api.model.vo.manager;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author lcysike
 * @Description: 商品Sku查询
 * @since 2019-10-06
 */
@Data
@ApiModel(value = "SkuStockVo对象", description = "商品Sku查询返回信息")
public class SkuStockVo implements Serializable {

    private Long id;

    @ApiModelProperty(value = "版本号", example = "1")
    private Long version;

    @ApiModelProperty(value = "产品id", example = "1")
    private Long productId;

    @ApiModelProperty(value = "sku编码")
    private String skuCode;

    @ApiModelProperty(value = "商品规格")
    private String specs;

    @ApiModelProperty(value = "商品sku重量")
    private BigDecimal weight;

    @ApiModelProperty(value = "展示图片")
    private String pic;

    @ApiModelProperty(value = "实售价")
    private BigDecimal price;

    @ApiModelProperty(value = "指导价（划线价）")
    private BigDecimal originalPrice;

    @ApiModelProperty(value = "库存", example = "100")
    private Integer stock;

    @ApiModelProperty(value = "预警库存", example = "10")
    private Integer lowStock;

    @ApiModelProperty(value = "销量", example = "50")
    private Integer sale;

    @ApiModelProperty(value = "限购数量", example = "10")
    private Integer perLimit;

}