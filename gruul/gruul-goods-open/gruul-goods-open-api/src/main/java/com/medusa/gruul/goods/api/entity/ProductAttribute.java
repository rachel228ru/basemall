package com.medusa.gruul.goods.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.medusa.gruul.common.data.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 商品属性
 * </p>
 *
 * @author lcysike
 * @since 2019-09-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_product_attribute")
@ApiModel(value = "ProductAttribute对象", description = "商品属性")
public class ProductAttribute extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 本店店铺id
     */
    @ApiModelProperty(value = "本店店铺id")
    @TableField("shop_id")
    private String shopId;

    /**
     * 产品id
     */
    @NotNull
    @ApiModelProperty(value = "产品id")
    @TableField("product_id")
    private Long productId;

    /**
     * 属性名称
     */
    @NotBlank
    @ApiModelProperty(value = "属性名称")
    @TableField("name")
    private String name;

    /**
     * 属性值
     */
    @ApiModelProperty(value = "属性值")
    @TableField("value")
    private String value;

}