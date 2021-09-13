package com.medusa.gruul.shops.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * @author create by zq
 * @date created in 2019/11/15
 */
@Data
@ApiModel(value = "ShopsRenovationPageVo 实体", description = "店铺装修模板页面实体 vo")
public class ShopsRenovationPageVo implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long id;


    /**
     * 归属关联Id
     */
    @ApiModelProperty(value = "归属关联Id")
    private String tenantId;


    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private String shopId;


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
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;


    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;


    /**
     * 聚合页面插件
     */
    @ApiModelProperty(value = "聚合页面插件")
    private List<ShopsRenovationAssemblyVo> assemblyVos;


    /**
     * modelId
     */
    @ApiModelProperty(value = "modelId")
    private String modelId;

}