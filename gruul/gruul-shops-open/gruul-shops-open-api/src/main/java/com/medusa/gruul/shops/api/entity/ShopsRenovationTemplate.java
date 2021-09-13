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
@TableName("t_shops_renovation_template")
@ApiModel(value = "店铺装修模板实体", description = "店铺装修模板表")
@Getter
@Setter
public class ShopsRenovationTemplate extends BaseEntity {


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
     * 模板类型 0自定义 1拼团 2商超
     */
    @NotNull
    @ApiModelProperty(value = "模板类型 0自定义 1拼团 2商超")
    @TableField("type")
    private String type;


    /**
     * 模板全局颜色 0红 1绿 2蓝
     */
    @NotNull
    @ApiModelProperty(value = "模板全局颜色 0红 1绿 2蓝")
    @TableField("colour")
    private String colour;


    /**
     * 模板是否使用中 0 否, 1 是
     */
    @NotNull
    @ApiModelProperty(value = "模板是否使用中 0 否, 1 是")
    @TableField("online_status")
    private String onlineStatus;


    /**
     *  默认模板 1是 0否
     */
    @NotNull
    @ApiModelProperty(value = " 默认模板 1是 0否")
    @TableField("is_dev_template")
    private String isDevTemplate;


    /**
     *  模板name
     */
    @NotNull
    @ApiModelProperty(value = " 模板name")
    @TableField("name")
    private String name;


    /**
     *  是否默认Copy的模板
     */
    @NotNull
    @ApiModelProperty(value = "是否默认Copy的模板")
    @TableField("is_copy_template")
    private String isCopyTemplate;


}