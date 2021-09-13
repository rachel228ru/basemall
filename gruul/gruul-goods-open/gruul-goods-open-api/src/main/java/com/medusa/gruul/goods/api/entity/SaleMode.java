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
 * 商品自定义专区
 * </p>
 *
 * @author lcysike
 * @since 2020-10-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_sale_mode")
@ApiModel(value = "SaleMode对象", description = "商品自定义专区")
public class SaleMode extends BaseEntity {

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
     * 专区名称
     */
    @ApiModelProperty(value = "专区名称")
    @TableField("mode_name")
    private String modeName;

    /**
     * 专区类型
     */
    @ApiModelProperty(value = "专区类型")
    @TableField("mode_type")
    private String modeType;

    /**
     * 排序id
     */
    @ApiModelProperty(value = "分区排序")
    @TableField("sort")
    private Integer sort;

}