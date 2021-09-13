package com.medusa.gruul.goods.api.model.vo.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 商品信息
 *
 * @author kyl
 * @since 2019-10-06
 */
@Data
@ApiModel(value = "ApiProductVo对象", description = "小程序商品查询返回信息")
public class ApiProductVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 供应商id
     */
    @ApiModelProperty(value = "供应商id")
    private Long providerId;

    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String providerName;

    /**
     * 配送方式(0--商家配送，1--快递配送 2--同城配送)
     */
    @ApiModelProperty(value = "配送方式(0--商家配送，1--快递配送 2--同城配送)")
    private Integer distributionMode;

    /**
     * 运费模板ID
     */
    @ApiModelProperty(value = "运费模板ID")
    private Long freightTemplateId;


    /**
     * 属性模版ID
     */
    @ApiModelProperty(value = "属性模版ID")
    private Long attributeId;

    /**
     * 属性模板名称
     */
    @ApiModelProperty(value = "属性模板名称")
    private String attributeName;

    /**
     * 限购类型(默认统一规格，0--统一规格，1--统一限购，2--规格限购)
     */
    @ApiModelProperty(value = "限购类型(默认统一规格，0--统一规格，1--统一限购，2--规格限购)")
    private Integer limitType;

    /**
     * 销售专区(默认商超系统，0--商超系统，2--限时秒杀)
     */
    @ApiModelProperty(value = "销售专区(默认商超系统，0--商超系统，2--限时秒杀)")
    private Long saleMode;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String name;

    /**
     * 展示图片
     */
    @ApiModelProperty(value = "展示图片")
    private String pic;

    /**
     * 宽屏展示图片
     */
    @ApiModelProperty(value = "宽屏展示图片")
    private String widePic;

    /**
     * 画册图片，连产品图片限制为6张，以逗号分割
     */
    @ApiModelProperty(value = "画册图片，连产品图片限制为6张，以逗号分割")
    private String albumPics;

    /**
     * 视频url
     */
    @ApiModelProperty(value = "视频url")
    private String videoUrl;

    /**
     * 货号
     */
    @ApiModelProperty(value = "货号")
    private String productSn;

    /**
     * 状态(默认上架，0--下架，1--上架)
     */
    @ApiModelProperty(value = "状态(默认上架，0--下架，1--上架)", example = "1")
    private Integer status;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序", example = "1")
    private Integer sort;

    /**
     * 销量
     */
    @ApiModelProperty(value = "销量", example = "100")
    private Integer sale;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    private String unit;

    /**
     * 商品重量，默认为克
     */
    @ApiModelProperty(value = "商品重量，默认为克")
    private BigDecimal weight;

    /**
     * 以逗号分割的产品服务：1->无忧退货；2->快速退款；3->免费包邮
     */
    @ApiModelProperty(value = "以逗号分割的产品服务：1->无忧退货；2->快速退款；3->免费包邮")
    private String serviceIds;

    /**
     * 商品详情
     */
    @ApiModelProperty(value = "商品详情")
    private String detail;

    /**
     * 规格是否展开
     */
    @ApiModelProperty(value = "规格是否展开")
    private Boolean openSpecs;

    /**
     * 销售属性
     */
    @ApiModelProperty(value = "销售属性")
    private String attribute;

    /**
     * 卖点描述
     */
    @ApiModelProperty(value = "卖点描述")
    private String saleDescribe;

    /**
     * 评分
     */
    @ApiModelProperty(value = "评分")
    private BigDecimal score;


    /**
     * 商品规格
     */
    @ApiModelProperty(value = "商品规格")
    private List<ApiSkuStockVo> skuStocks;

    /**
     * 商品属性
     */
    @ApiModelProperty(value = "商品属性")
    private List<ApiProductAttributeVo> productAttributes;

    /**
     * 商品展示分类信息
     */
    @ApiModelProperty(value = "商品展示分类信息")
    private List<ApiProductShowCategoryVo> productShowCategorys;

}