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
 * 商品展示分类
 * </p>
 *
 * @author lcysike
 * @since 2019-10-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_product_show_category")
@ApiModel(value = "ProductShowCategory对象", description = "商品展示分类")
public class ProductShowCategory extends BaseEntity {

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
     * 上级分类的编号：0表示一级分类
     */
    @ApiModelProperty(value = "上级分类的编号：0表示一级分类")
    @TableField("parent_id")
    private Long parentId;

    /**
     * 产品id
     */
    @NotNull
    @ApiModelProperty(value = "产品id")
    @TableField("product_id")
    private Long productId;

    /**
     * 商品展示分类id
     */
    @NotBlank
    @ApiModelProperty(value = "商品展示分类id")
    @TableField("show_category_id")
    private Long showCategoryId;

}