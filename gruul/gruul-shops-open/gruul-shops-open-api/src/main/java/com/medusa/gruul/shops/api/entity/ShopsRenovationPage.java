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
@TableName("t_shops_renovation_page")
@ApiModel(value = "店铺装修模板页面实体", description = "店铺装修模板页面表")
@Getter
@Setter
public class ShopsRenovationPage extends BaseEntity {


    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 店铺id
     */
    @NotNull
    @ApiModelProperty(value = "店铺id")
    @TableField("shop_id")
    private String shopId;


    /**
     * 所属模板ID
     */
    @NotNull
    @ApiModelProperty(value = "所属模板ID")
    @TableField("template_id")
    private Long templateId;


    /**
     * 分类页
     */
    @NotNull
    @TableField("type")
    @ApiModelProperty(value = "分类页")
    private String type;


    /**
     * 自定义页面name
     */
    @NotNull
    @ApiModelProperty(value = "自定义页面name ")
    @TableField("page_name")
    private String pageName;


    /**
     * 是否默认页面
     */
    @ApiModelProperty(value = "是否默认页面")
    private String isDef;


    /**
     * modelId
     */
    @NotNull
    @ApiModelProperty(value = "modelId")
    @TableField("model_id")
    private String modelId;

}