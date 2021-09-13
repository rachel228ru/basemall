package com.medusa.gruul.goods.api.model.dto.manager;

import cn.hutool.core.bean.BeanUtil;
import com.medusa.gruul.goods.api.entity.ProductShowCategory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * <p>
 * 展示分类
 * </p>
 *
 * @author lcysike
 * @since 2019-10-03
 */
@Data
@ApiModel(value = "新增产品展示分类大类DTO")
public class ProductShowCategoryDto {

    private Long id;

    @ApiModelProperty(value = "产品id")
    private Long productId;

    @NotNull
    @ApiModelProperty(value = "上级分类的编号：0表示一级分类")
    private Long parentId;

    @ApiModelProperty(value = "展示分类id")
    private Long showCategoryId;

    @ApiModelProperty(value = "下级分类")
    private List<ProductShowCategorySecondDto> productShowCategorySeconds;

    public ProductShowCategory coverProductShowCategory() {
        ProductShowCategory productShowCategory = new ProductShowCategory();
        BeanUtil.copyProperties(this, productShowCategory);
        return productShowCategory;
    }
}