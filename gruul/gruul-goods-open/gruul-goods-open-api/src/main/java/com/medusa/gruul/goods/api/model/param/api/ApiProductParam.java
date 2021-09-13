package com.medusa.gruul.goods.api.model.param.api;

import com.medusa.gruul.common.core.param.QueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 小程序商品查询参数
 *
 * @author kyl
 * @since 2019-10-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ApiProductParam对象", description = "小程序商品查询参数")
public class ApiProductParam extends QueryParam {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "商品展示分类id")
    private Long showCategoryId;

    @ApiModelProperty(value = "商品展示分类名称")
    private String showCategoryName;

    @ApiModelProperty(value = "商品销售专区")
    private Long saleMode;


    @ApiModelProperty(value = "创建开始时间")
    private String createBeginTime;

    @ApiModelProperty(value = "创建结束时间")
    private String createEndTime;

    @ApiModelProperty(value = "排序类型")
    private Integer type;

    @ApiModelProperty(value = "排序规则(升序-asc 降序-desc)")
    private String sort;
}