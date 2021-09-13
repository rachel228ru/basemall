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
import javax.validation.constraints.Size;

/**
 * <p>
 * 展示分类
 * </p>
 *
 * @author alan
 * @since 2019-09-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_show_category")
@ApiModel(value = "ShowCategory对象", description = "展示分类")
public class ShowCategory extends BaseEntity {

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
     * 上机分类的编号：0表示一级分类
     */
    @ApiModelProperty(value = "上机分类的编号：0表示一级分类")
    @TableField("parent_id")
    private Long parentId;

    /**
     * 分类名称
     */
    @NotBlank
    @Size(max = 64)
    @ApiModelProperty(value = "分类名称")
    @TableField("name")
    private String name;

    /**
     * 分类级别：0->1级；1->2级
     */
    @NotNull
    @ApiModelProperty(value = "分类级别：0->1级；1->2级")
    @TableField("level")
    private Integer level;

    /**
     * 销售专区
     */
    @ApiModelProperty(value = "销售专区")
    @TableField("sale_mode")
    private Long saleMode;

    /**
     * 分类排序
     */
    @ApiModelProperty(value = "分类排序")
    @TableField("sort")
    private Integer sort;

}