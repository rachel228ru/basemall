package com.medusa.gruul.goods.api.model.vo.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 商品规格信息
 *
 * @author kyl
 * @since 2019-10-06
 */
@Data
@ApiModel(value = "ApiSkuStockVo对象", description = "小程序商品Sku查询返回信息")
public class ApiSkuStockVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 版本号
     */
    @ApiModelProperty(value = "版本号")
    private Long version;

    /**
     * 产品id
     */
    @ApiModelProperty(value = "产品id")
    private Long productId;

    /**
     * sku编码
     */
    @ApiModelProperty(value = "sku编码")
    private String skuCode;

    /**
     * 商品规格
     */
    @ApiModelProperty(value = "商品规格")
    private String specs;

    /**
     * 商品sku重量
     */
    @ApiModelProperty(value = "商品sku重量")
    private BigDecimal weight;

    /**
     * 展示图片
     */
    @ApiModelProperty(value = "展示图片")
    private String pic;

    /**
     * 实售价
     */
    @ApiModelProperty(value = "实售价")
    private BigDecimal price;

    /**
     * 指导价（划线价）
     */
    @ApiModelProperty(value = "指导价（划线价）")
    private BigDecimal originalPrice;

    /**
     * 库存
     */
    @ApiModelProperty(value = "库存", example = "100")
    private Integer stock;

    /**
     * 预警库存
     */
    @ApiModelProperty(value = "预警库存", example = "10")
    private Integer lowStock;

    /**
     * 销量
     */
    @ApiModelProperty(value = "销量", example = "50")
    private Integer sale;

    /**
     * 限购数量
     */
    @ApiModelProperty(value = "限购数量", example = "10")
    private Integer perLimit;


}