package com.medusa.gruul.goods.api.model.dto.manager;

import cn.hutool.core.bean.BeanUtil;
import com.medusa.gruul.goods.api.entity.Product;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;


/**
 * <p>
 * 导入商品信息
 * </p>
 *
 * @author lcysike
 * @since 2020-05-07
 */
@Data
@ApiModel(value = "导入商品信息DTO")
public class ImportProductDto {

    @ApiModelProperty(value = "供应商id")
    private Long providerId;

    @NotNull
    @ApiModelProperty(value = "配送方式(0--商家配送，1--快递配送)")
    private Integer distributionMode;

    @ApiModelProperty(value = "运费模板ID")
    private Long freightTemplateId;


    @ApiModelProperty(value = "属性模版ID")
    private Long attributeId;

    @ApiModelProperty(value = "属性模板名称")
    private String attributeName;

    @NotNull
    @ApiModelProperty(value = "限购类型(默认统一规格，0--统一规格，1--统一限购，2--规格限购)")
    private Integer limitType;

    @NotNull
    @ApiModelProperty(value = "销售专区(默认商超系统，0--商超系统，2--限时秒杀)")
    private Long saleMode;

    @NotBlank
    @ApiModelProperty(value = "商品名称")
    private String name;

    @NotBlank
    @ApiModelProperty(value = "展示图片")
    private String pic;

    @ApiModelProperty(value = "宽屏展示图片")
    private String widePic;

    @NotBlank
    @ApiModelProperty(value = "画册图片，连产品图片限制为6张，以逗号分割")
    private String albumPics;

    @ApiModelProperty(value = "视频url")
    private String videoUrl;

    @ApiModelProperty(value = "货号")
    private String productSn;

    @ApiModelProperty(value = "状态(默认上架，0--下架（仓库中），1--上架，2--已售完)")
    private Integer status;

    @ApiModelProperty(value = "销量")
    private Integer sale;

    @ApiModelProperty(value = "单位")
    private String unit;

    @NotNull
    @ApiModelProperty(value = "商品重量，默认为克")
    private BigDecimal weight;

    @ApiModelProperty(value = "以逗号分割的产品服务：1->无忧退货；2->快速退款；3->免费包邮")
    private String serviceIds;

    @ApiModelProperty(value = "商品详情")
    private String detail;

    @NotNull
    @ApiModelProperty(value = "规格是否展开")
    private Boolean openSpecs;

    @ApiModelProperty(value = "销售属性")
    private String attribute;

    @ApiModelProperty(value = "评分")
    private BigDecimal score;

    @ApiModelProperty(value = "商品sku信息")
    private List<SkuStockDto> skuStocks;

    @ApiModelProperty(value = "商品属性信息")
    private List<ProductAttributeDto> productAttributes;

    @ApiModelProperty(value = "商品展示分类信息")
    private List<ProductShowCategoryDto> productShowCategorys;

    public Product coverProduct() {
        Product product = new Product();
        BeanUtil.copyProperties(this, product);
        return product;
    }
}