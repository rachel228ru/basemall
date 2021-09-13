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
 * 平台租户菜单表
 * </p>
 *
 * @author whh
 * @since 2020-01-09
 */
@Data
@Accessors(chain = true)
@TableName("t_platform_tenement_menu")
@ApiModel(value = "TenementMenu对象", description = "平台租户菜单表")
public class TenementMenu {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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
     * 小程序id
     */
    @ApiModelProperty(value = "小程序id")
    @TableField("mini_id")
    private Long miniId;

    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    @TableField("tenant_id")
    private String tenantId;


    /**
     * 是否有权限使用 0-可使用 1不可使用
     */
    @ApiModelProperty(value = "是否有权限使用 0-可使用 1不可使用")
    @TableField("permission_status")
    private Integer permissionStatus;

    /**
     * 显示状态 0->显示 1->隐藏
     */
    @ApiModelProperty(value = "显示状态 0->显示 1->隐藏")
    @TableField("hide_status")
    private Integer hideStatus;

    /**
     * 基础菜单对应id
     */
    @ApiModelProperty(value = "基础菜单对应id")
    @TableField("base_menu_id")
    private Long baseMenuId;

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
    private Integer deleted;
}
