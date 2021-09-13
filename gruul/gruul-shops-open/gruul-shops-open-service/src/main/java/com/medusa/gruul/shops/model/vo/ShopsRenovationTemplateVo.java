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
@ApiModel(value = "ShopsRenovationTemplateVo 实体", description = "店铺装修模板实体 vo")
public class ShopsRenovationTemplateVo implements Serializable {

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
     * 模板类型 0自定义 1拼团 2商超
     */
    @ApiModelProperty(value = "模板类型 0自定义 1拼团 2商超")
    private String type;


    /**
     * 模板全局颜色 0红 1绿 2蓝
     */
    @ApiModelProperty(value = "模板全局颜色 0红 1绿 2蓝")
    private String colour;


    /**
     * 模板是否使用中 0 否, 1 是
     */
    @ApiModelProperty(value = "模板是否使用中 0 否, 1 是")
    private String onlineStatus;


    /**
     *  默认模板 1是 0否
     */
    @ApiModelProperty(value = " 默认模板 1是 0否")
    private String isDevTemplate;


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
     *  模板name
     */
    @ApiModelProperty(value = " 模板name")
    private String name;


    /**
     * 归属聚合页面
     */
    @ApiModelProperty(value = "归属聚合页面")
    private List pages;


    /**
     *  归属聚合控件
     */
    @ApiModelProperty(value = " 归属聚合控件")
    private List plugins;
}