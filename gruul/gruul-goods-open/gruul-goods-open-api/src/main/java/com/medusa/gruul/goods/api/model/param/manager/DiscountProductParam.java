package com.medusa.gruul.goods.api.model.param.manager;

import com.medusa.gruul.common.core.param.QueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;


/**
 * @author lcysike
 * @Description: 商品
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "DiscountProductParam对象", description = "商品查询参数")
public class DiscountProductParam extends QueryParam {

    @ApiModelProperty(value = "货号")
    private Long productSn;

    @ApiModelProperty(value = "销售专区")
    private Long saleMode;

    @ApiModelProperty(value = "展示分类id")
    private Long showCategoryId;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "最小价格")
    private BigDecimal minPrice;

    @ApiModelProperty(value = "最大价格")
    private BigDecimal maxPrice;

    @ApiModelProperty(value = "发货单id")
    private String deliverId;

    @ApiModelProperty(value = "匹配的商品list")
    private List<Long> productIds;

}