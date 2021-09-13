package com.medusa.gruul.shops.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author whh
 * @description
 * @data: 2019/12/13
 */
@Data
public class AccountCenterMenuDto implements Serializable {

    private static final long serialVersionUID = -5305291981282924071L;

    @ApiModelProperty(value = "菜单id")
    private Long menuId;

    @ApiModelProperty(value = "菜单名称")
    private String menuName;

    @ApiModelProperty(value = "菜单当前图标url")
    private String menuIconUrl;

    @ApiModelProperty(value = "默认图标地址")
    private String defaultIcon;

    @ApiModelProperty(value = "菜单样式类别 1-展开式 2-折叠式")
    private Integer styleType;

    @ApiModelProperty(value = "排序位置")
    private Integer sortIndex;

    @ApiModelProperty(value = "父级id")
    private Long pId;

    @ApiModelProperty(value = "菜单是否 0隐藏1显示")
    private Boolean hideMenu;

    @ApiModelProperty(value = "底部是否存在分割线")
    private Boolean splitFlag;

    @ApiModelProperty(value = "是否可用 0不可用 1-可用")
    private Integer allowUse;

    @ApiModelProperty(value = "跳转类型前端决定")
    private String linkSelectItem;

    @ApiModelProperty(value = "子菜单")
    private List<AccountCenterMenuDto> subMenu;
}
