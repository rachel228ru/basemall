package com.medusa.gruul.goods.api.model.vo.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品展示分类二级分类VO
 *
 * @author kyl
 * @since 2019-11-05
 */
@Data
@ApiModel(value = "ApiShowCategorySecondVo对象", description = "小程序商品展示分类二级分类")
public class ApiShowCategorySecondVo {
    private Long id;

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "商品数量")
    private String productNumber;
}