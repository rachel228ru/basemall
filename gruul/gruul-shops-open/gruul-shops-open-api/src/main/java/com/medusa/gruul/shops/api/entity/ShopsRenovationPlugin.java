package com.medusa.gruul.shops.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.medusa.gruul.common.data.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;


/**
 * @author create by zq
 * @date created in 2019/11/15
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("t_shops_renovation_plugin")
@ApiModel(value = "店铺装修页面全局控件属性实体", description = "店铺装修页面全局控件属性表")
@Getter
@Setter
public class ShopsRenovationPlugin extends BaseEntity {


    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 所属模板ID
     */
    @NotNull
    @ApiModelProperty(value = "所属模板ID")
    @TableField("template_id")
    private Long templateId;


    /**
     * 店铺id
     */
    @NotNull
    @ApiModelProperty(value = "店铺id")
    @TableField("shop_id")
    private String shopId;


    /**
     * 控件JSON
     */
    @NotNull
    @ApiModelProperty(value = "控件JSON")
    @TableField("plugin_properties")
    private String pluginProperties;


    /**
     * 备用字段
     */
    @NotNull
    @ApiModelProperty(value = "备用字段")
    @TableField("spare")
    private String spare;


    /**
     * 控件中文名称
     */
    @NotNull
    @ApiModelProperty(value = "控件中文名称")
    @TableField("plugin_name_cn")
    private String pluginNameCn;


    /**
     * 控件英文名称
     */
    @NotNull
    @ApiModelProperty(value = "控件英文名称")
    @TableField("plugin_name_en")
    private String pluginNameEn;


    /**
     * 是否选中 0否 1是
     */
    @NotNull
    @ApiModelProperty(value = "是否选中 0否 1是")
    @TableField("is_selection")
    private String isSelection;


    /**
     * 是否必选 0否 1是
     */
    @NotNull
    @ApiModelProperty(value = "是否必选 0否 1是")
    @TableField("is_mandatory")
    private String isMandatory;
}