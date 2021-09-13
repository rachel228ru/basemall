package com.medusa.gruul.goods.api.model.vo.manager;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description: ItemVo.java
 * @author: alan
 * @date: 2019/11/5 21:27
 */
@Data
@ApiModel(value = "所选商品详情")
public class ItemVo implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id", example = "1")
    private Long productId;

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
     * 商品编号
     */
    @ApiModelProperty(value = "商品编号")
    private String productSn;

    /**
     * 销售价格
     */
    @ApiModelProperty(value = "销售价格")
    private BigDecimal productPrice;

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
     * 商品sku编号
     */
    @ApiModelProperty(value = "商品sku编号", example = "1")
    private Long productSkuId;

    /**
     * 商品sku条码
     */
    @ApiModelProperty(value = "商品sku条码")
    private String productSkuCode;


    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片")
    private String productSkuPic;


    /**
     * 商品的销售属性
     */
    @ApiModelProperty(value = "商品的销售属性")
    private String specs;

    /**
     * 配送方式(0--商家配送，1--快递配送 2--同城配送)
     */
    @ApiModelProperty(value = "配送方式(0--商家配送，1--快递配送 2--同城配送)")
    private Integer distributionMode;

    /**
     * 商品的运费模版id
     */
    @ApiModelProperty(value = "运费模板ID")
    private Long freightTemplateId;

    /**
     * 库存状态
     */
    @ApiModelProperty(value = "库存状态：0->有库存；1->无库存")
    private Integer status;

    /**
     * 限购类型(默认统一规格，0--统一规格，1--统一限购，2--规格限购)
     */
    @ApiModelProperty(value = "限购类型(默认统一规格，0--统一规格，1--统一限购，2--规格限购)")
    private Integer limitType;

    /**
     * 限购数量
     */
    @ApiModelProperty(value = "限购数量")
    private Integer perLimit;

    /**
     * 商品重量，默认为克
     */
    @ApiModelProperty(value = "商品重量，默认为克")
    private BigDecimal weight;

    /**
     * 供应商id
     */
    @ApiModelProperty(value = "供应商id")
    private Long providerId;


}
