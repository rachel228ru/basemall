package com.medusa.gruul.shops.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author whh
 * @description
 * @data: 2019/11/21
 */
@Data
public class AccountCenterSettingDto implements Serializable {

    private static final long serialVersionUID = -7526899743761309130L;

    @ApiModelProperty(value = "用户中心数据id,只有修改的时候才需传入改值")
    private Integer id;

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

    @ApiModelProperty(value = "租户id")
    private String tenantId;

    @ApiModelProperty(value = "一级菜单")
    private List<AccountCenterMenuDto> menuVos;

}
