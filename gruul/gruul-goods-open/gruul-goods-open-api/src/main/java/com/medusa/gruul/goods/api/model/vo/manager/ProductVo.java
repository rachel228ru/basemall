package com.medusa.gruul.goods.api.model.vo.manager;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


/**
 * <p>
 * 商品信息Vo
 * </p>
 *
 * @author lcysike
 * @since 2019-10-06
 */
@Data
@ApiModel(value = "ProductVo对象", description = "商品查询返回信息")
public class ProductVo implements Serializable {

    private Long id;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "商品分拣分类名称")
    private String sortingCategoryName;

    @ApiModelProperty(value = "供应商id")
    private Long providerId;

    @ApiModelProperty(value = "供应商名称")
    private String providerName;

    @ApiModelProperty(value = "配送方式(0--商家配送，1--快递配送 2--同城配送)")
    private Integer distributionMode;

    @ApiModelProperty(value = "运费模板ID")
    private Long freightTemplateId;

    @ApiModelProperty(value = "属性模版ID")
    private Long attributeId;

    @ApiModelProperty(value = "属性模板名称")
    private String attributeName;

    @ApiModelProperty(value = "限购类型(默认统一规格，0--统一规格，1--统一限购，2--规格限购)")
    private Integer limitType;

    @ApiModelProperty(value = "销售专区(默认商超系统，0--商超系统，2--限时秒杀)")
    private Long saleMode;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "展示图片")
    private String pic;

    @ApiModelProperty(value = "宽屏展示图片")
    private String widePic;

    @ApiModelProperty(value = "画册图片，连产品图片限制为6张，以逗号分割")
    private String albumPics;

    @ApiModelProperty(value = "视频url")
    private String videoUrl;

    @ApiModelProperty(value = "货号")
    private String productSn;

    @ApiModelProperty(value = "状态(默认上架，0--下架，1--上架)")
    private Integer status;

    @ApiModelProperty(value = "商品位置(默认线上，0--线上，1--素材库)")
    private Integer place;

    @ApiModelProperty(value = "电商平台链接")
    private String csvUrl;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "销量")
    private Integer sale;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "商品重量，默认为克")
    private BigDecimal weight;

    @ApiModelProperty(value = "以逗号分割的产品服务：1->无忧退货；2->快速退款；3->免费包邮")
    private String serviceIds;

    @ApiModelProperty(value = "商品详情")
    private String detail;

    @ApiModelProperty(value = "规格是否展开")
    private Boolean openSpecs;

    @ApiModelProperty(value = "销售属性")
    private String attribute;

    @ApiModelProperty(value = "卖点描述")
    private String saleDescribe;

    @ApiModelProperty(value = "评分")
    private BigDecimal score;

    @ApiModelProperty(value = "商品sku信息")
    private List<SkuStockVo> skuStocks;

    @ApiModelProperty(value = "商品属性信息")
    private List<ProductAttributeVo> productAttributes;

    @ApiModelProperty(value = "商品展示分类信息")
    private List<ProductShowCategoryVo> productShowCategorys;

}