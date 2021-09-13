package com.medusa.gruul.goods.api.model.dto.manager;

import cn.hutool.core.bean.BeanUtil;
import com.medusa.gruul.goods.api.entity.ProductAttribute;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 商品属性
 * </p>
 *
 * @author lcysike
 * @since 2019-10-03
 */
@Data
@ApiModel(value = "新增或修改商品属性DTO")
public class ProductAttributeDto {

    private Long id;

    @ApiModelProperty(value = "产品id")
    private Long productId;

    @NotBlank
    @ApiModelProperty(value = "属性名称")
    private String name;

    @ApiModelProperty(value = "属性值")
    private String value;

    public ProductAttribute coverProductAttribute() {
        ProductAttribute productAttribute = new ProductAttribute();
        BeanUtil.copyProperties(this, productAttribute);
        return productAttribute;
    }
}