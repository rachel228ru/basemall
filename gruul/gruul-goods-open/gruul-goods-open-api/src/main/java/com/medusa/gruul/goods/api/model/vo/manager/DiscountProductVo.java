package com.medusa.gruul.goods.api.model.vo.manager;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * <p>
 * 商品信息Vo
 * </p>
 *
 * @author lcysike
 * @since 2019-10-06
 */
@Data
@ApiModel(value = "DiscountProductVo对象", description = "商品查询返回信息")
public class DiscountProductVo implements Serializable {

    private Long id;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "展示图片")
    private String pic;

    @ApiModelProperty(value = "宽屏展示图片")
    private String widePic;

    @ApiModelProperty(value = "画册图片，连产品图片限制为6张，以逗号分割")
    private String albumPics;

    @ApiModelProperty(value = "货号")
    private Long productSn;

    @ApiModelProperty(value = "规格类型")
    private Integer limitType;

    @ApiModelProperty(value = "最小价格")
    private BigDecimal minPrice;

    @ApiModelProperty(value = "最大价格")
    private BigDecimal maxPrice;

    @ApiModelProperty(value = "销售专区")
    private Long saleMode;
}