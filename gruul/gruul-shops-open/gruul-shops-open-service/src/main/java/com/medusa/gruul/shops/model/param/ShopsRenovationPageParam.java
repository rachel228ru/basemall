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
@ApiModel(value = "ShopsRenovationPageParam 实体", description = "店铺装修模板页面请求参数实体")
public class ShopsRenovationPageParam extends QueryParam {


    @ApiModelProperty("id")
    private Long id;


    /**
     * 所属模板ID
     */
    @ApiModelProperty(value = "所属模板ID")
    private Long templateId;


    /**
     * 自定义页面name
     */
    @ApiModelProperty(value = "自定义页面name ")
    private String pageName;


    /**
     * 逻辑删除标识  0正常 1已删除
     */
    @ApiModelProperty(value = "逻辑删除标识  0正常 1已删除")
    private String isDeleted;


    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private String shopId;


    /**
     * 是否默认页面
     */
    @ApiModelProperty(value = "是否默认页面")
    private String isDef;


    /**
     * 分类页
     */
    @ApiModelProperty(value = "分类页")
    private String type;


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
     * modelId
     */
    @ApiModelProperty(value = "modelId")
    private String modelId;

}
