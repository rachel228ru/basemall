package com.medusa.gruul.goods.api.model.vo.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 商品展示分类下商品列表
 *
 * @author lcy
 * @since 2020-09-14
 */
@Data
@ApiModel(value = "ApiShowCategoryProductVo对象", description = "小程序展示分类下商品列表")
public class ApiShowCategoryProductVo {
    private Long id;

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "下级分类")
    private List<ApiAliveProductVo> apiAliveProductVos;
}