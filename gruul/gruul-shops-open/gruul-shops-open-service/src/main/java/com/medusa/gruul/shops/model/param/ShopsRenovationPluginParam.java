package com.medusa.gruul.shops.model.param;

import com.medusa.gruul.common.core.param.QueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author create by zq
 * @date created in 2019/11/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ShopsRenovationPluginParam 实体", description = "店铺装修模板全局控件请求参数实体")
public class ShopsRenovationPluginParam extends QueryParam {


    @ApiModelProperty("id")
    private Long id;


    /**
     * 所属模板ID
     */
    @ApiModelProperty(value = "所属模板ID")
    private Long templateId;


    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private String shopId;


    /**
     * 控件JSON
     */
    @ApiModelProperty(value = "控件JSON")
    private String pluginProperties;


    /**
     * 逻辑删除标识  0正常 1已删除
     */
    @ApiModelProperty(value = "逻辑删除标识  0正常 1已删除")
    private String isDeleted;


    /**
     * 归属关联Id
     */
    @ApiModelProperty(value = "归属关联Id")
    private String tenantId;


    /**
     * 操作人id
     */
    @ApiModelProperty(value = "操作人id")
    private String operatorId;


    /**
     * 操作人name
     */
    @ApiModelProperty(value = "操作人name")
    private String operatorName;


    /**
     * 备用字段
     */
    @ApiModelProperty(value = "备用字段")
    private String spare;


    /**
     * 控件名称中文
     */
    @ApiModelProperty(value = "控件名称中文")
    private String pluginNameCn;

    /**
     * 控件名称英文
     */
    @ApiModelProperty(value = "控件名称英文")
    private String pluginNameEn;


    /**
     * 是否选中 0否 1是
     */
    @ApiModelProperty(value = "是否选中 0否 1是")
    private String isSelection;


    /**
     * 是否允许取消 0否 1是
     */
    @ApiModelProperty(value = "是否允许取消 0否 1是")
    private String isMandatory;
}
