package com.medusa.gruul.goods.api.model.dto.manager;

import cn.hutool.core.bean.BeanUtil;
import com.medusa.gruul.goods.api.entity.ProductShowCategory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * <p>
 * 展示分类
 * </p>
 *
 * @author lcysike
 * @since 2019-10-03
 */
@Data
@ApiModel(value = "新增展示分类二级分类DTO")
public class ProductShowCategorySecondDto {

    private Long id;

    @ApiModelProperty(value = "产品id")
    private Long productId;

    @ApiModelProperty(value = "上机分类的编号：0表示一级分类")
    @NotNull
    private Long parentId;

    @ApiModelProperty(value = "展示分类id")
    private String showCategoryId;


    public ProductShowCategory coverProductShowCategory() {
        ProductShowCategory productShowCategory = new ProductShowCategory();
        BeanUtil.copyProperties(this, productShowCategory);
        return productShowCategory;
    }
}