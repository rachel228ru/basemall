package com.medusa.gruul.platform.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 * @description
 * @data: 2020/1/9
 */
@Data
public class MenuDto {

    @ApiModelProperty(value = "菜单Id")
    private Long menuId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "路径")
    private String path;

    @ApiModelProperty(value = "name")
    private String name;

    @ApiModelProperty(value = "父级id")
    private Long pId;

    @ApiModelProperty(value = "服务编号")
    private String serviceNumber;

    @ApiModelProperty(value = "指定服务id,手动维护")
    private String serviceId;

    @ApiModelProperty(value = "对应版本 模板库代码id")
    private Long templateCodeId;

    @ApiModelProperty(value = "菜单图标")
    private String icon;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "是否有权限使用 0-可使用 1不可使用")
    private Integer permissionStatus;

    @ApiModelProperty(value = "显示状态 0->显示 1->隐藏")
    private Integer hideStatus;
}
