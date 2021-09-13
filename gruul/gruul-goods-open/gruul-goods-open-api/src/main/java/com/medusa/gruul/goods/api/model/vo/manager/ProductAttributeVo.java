package com.medusa.gruul.goods.api.model.vo.manager;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 商品属性
 * </p>
 *
 * @author lcysike
 * @since 2019-10-03
 */
@Data
@ApiModel(value = "ProductAttribute对象", description = "商品属性查询返回信息")
public class ProductAttributeVo implements Serializable {

    private Long id;

    @ApiModelProperty(value = "产品id")
    private Long productId;

    @ApiModelProperty(value = "属性名称")
    private String name;

    @ApiModelProperty(value = "属性值")
    private String value;
}