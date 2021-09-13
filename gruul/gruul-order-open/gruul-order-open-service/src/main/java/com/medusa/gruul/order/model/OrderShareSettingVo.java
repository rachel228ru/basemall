package com.medusa.gruul.order.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * The type Order share setting vo.
 */
@ApiModel("设置分享晒单设置")
@Data
public class OrderShareSettingVo {
    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     * 背景图
     */
    @ApiModelProperty(value = "背景图")
    private String background;

    /**
     * 是否重置为默认值
     */
    @NotNull
    @ApiModelProperty(value = "是否重置为默认值")
    private boolean defaultValue;

}
