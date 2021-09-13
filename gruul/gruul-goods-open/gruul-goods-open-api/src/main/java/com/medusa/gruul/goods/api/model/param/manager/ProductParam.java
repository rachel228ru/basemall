package com.medusa.gruul.goods.api.model.param.manager;

import com.medusa.gruul.common.core.param.QueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;


/**
 * @author lcysike
 * @Description: 商品
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ProductParam对象", description = "商品查询参数")
public class ProductParam extends QueryParam {

    @ApiModelProperty(value = "货号")
    private String productSn;

    @ApiModelProperty(value = "销售专区")
    @NotBlank(message = "请选择专区")
    private Long saleMode;

    @ApiModelProperty(value = "展示分类")
    private Long showCategoryId;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "状态(默认上架，0--下架，1--上架 )")
    private Integer status;

    @ApiModelProperty(value = "商品位置(默认线上，0--线上，1--素材库)")
    private Integer place;

    @ApiModelProperty(value = "供应商id")
    private String providerId;

    @ApiModelProperty(value = "供应商")
    private String providerName;

    @ApiModelProperty(value = "创建开始时间")
    private String createBeginTime;

    @ApiModelProperty(value = "创建结束时间")
    private String createEndTime;

    @ApiModelProperty(value = "最小价格")
    private BigDecimal minPrice;

    @ApiModelProperty(value = "最大价格")
    private BigDecimal maxPrice;
}