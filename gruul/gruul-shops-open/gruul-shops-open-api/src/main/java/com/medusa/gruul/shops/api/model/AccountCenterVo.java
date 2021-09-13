package com.medusa.gruul.shops.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author whh
 * @description
 * @data: 2019/11/20
 */
@Data
public class AccountCenterVo {

    @ApiModelProperty(value = "用户中心数据id")
    private Long id;

    @ApiModelProperty(value = "头部风格 1-系统风格 2-自定义风格")
    private Integer headStyle;

    @ApiModelProperty(value = "自定义风格样式,json存储")
    private String customStyle;

    @ApiModelProperty(value = "领卡文案")
    private String getCartText;

    @ApiModelProperty(value = "非会员隐藏领卡入口 0-隐藏 1-显示")
    private Boolean hideCartInlet;

    @ApiModelProperty(value = "订单icon信息 json存储")
    private String orderInfo;

    @ApiModelProperty(value = "菜单选择样式 1-展开式 2-折叠式")
    private Integer menuStyle;

    @ApiModelProperty(value = "菜单")
    private List<MenuVo> menuVos;
}
