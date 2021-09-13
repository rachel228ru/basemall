package com.medusa.gruul.goods.api.model.vo.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 商品展示分类一级分类VO
 *
 * @author kyl
 * @since 2019-11-05
 */
@Data
@ApiModel(value = "ApiShowCategoryVo对象", description = "小程序商品展示分类一级分类")
public class ApiShowCategoryVo {
    private Long id;

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "下级分类")
    private List<ApiShowCategorySecondVo> showCategoryVos;
}