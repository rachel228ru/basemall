package com.medusa.gruul.platform.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 菜单基础版本维护
 * </p>
 *
 * @author whh
 * @since 2020-01-09
 */
@Data
@Accessors(chain = true)
@TableName("t_base_menu")
@ApiModel(value = "BaseMenu对象", description = "菜单基础版本维护")
public class BaseMenu {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    @TableField("title")
    private String title;

    /**
     * 路径
     */
    @ApiModelProperty(value = "路径")
    @TableField("path")
    private String path;

    /**
     * name
     */
    @ApiModelProperty(value = "name")
    @TableField("name")
    private String name;

    /**
     * 父级id
     */
    @ApiModelProperty(value = "父级id")
    @TableField("p_id")
    private Long pId;

    /**
     * 服务编号
     */
    @ApiModelProperty(value = "服务编号")
    @TableField("service_number")
    private String serviceNumber;

    /**
     * 指定服务id,手动维护
     */
    @ApiModelProperty(value = "指定服务id,手动维护")
    @TableField("service_id")
    private String serviceId;

    /**
     * 对应版本 模板库代码id
     */
    @ApiModelProperty(value = "对应版本 模板库代码id")
    @TableField("template_code_id")
    private Long templateCodeId;

    /**
     * 菜单图标
     */
    @ApiModelProperty(value = "菜单图标")
    @TableField("icon")
    private String icon;

    /**
     * 菜单编码
     */
    @ApiModelProperty(value = "菜单编码")
    @TableField("code")
    private String code;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    @TableField("description")
    private String description;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    @ApiModelProperty(value = "删除状态：0->未删除；1->已删除")
    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    private Boolean deleted;
}
