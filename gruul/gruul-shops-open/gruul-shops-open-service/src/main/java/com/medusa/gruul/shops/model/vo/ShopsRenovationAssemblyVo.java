package com.medusa.gruul.shops.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * @author create by zq
 * @date created in 2019/11/15
 */
@Data
@ApiModel(value = "ShopsRenovationAssemblyVo 实体", description = "店铺装修模板页面组件实体 vo")
public class ShopsRenovationAssemblyVo implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long id;


    /**
     * 所属页面ID
     */
    @ApiModelProperty(value = "所属页面ID")
    private Long pageId;


    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private String shopId;


    /**
     * 组件序列
     */
    @ApiModelProperty(value = "组件序列")
    private Long order;


    /**
     * 组件属性 json串
     */
    @ApiModelProperty(value = "组件属性 json串")
    private String properties;


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
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;


    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

}