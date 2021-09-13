package com.medusa.gruul.goods.api.model.vo.manager;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 商品展示分类二级分类vo
 * </p>
 *
 * @author lcysike
 * @since 2019-10-03
 */
@Data
@ApiModel(value = "ProductShowCategorySecondVo对象", description = "商品展示分类二级分类查询返回信息")
public class ProductShowCategorySecondVo implements Serializable {

    private Long id;

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "上级分类的编号：0表示一级分类")
    private Long parentId;

    @ApiModelProperty(value = "展示分类id")
    private Long showCategoryId;
}