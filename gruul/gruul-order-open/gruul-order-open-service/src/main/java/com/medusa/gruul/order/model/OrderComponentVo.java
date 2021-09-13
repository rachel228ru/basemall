package com.medusa.gruul.order.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * The type Order component vo.
 * <p>
 * 自定义组件
 *
 * @author alan
 * @date 2019 /11/5 21:29
 */
@Data
@ApiModel(value = "自定义组件")
public class OrderComponentVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;
    /**
     * 类型
     */
    @ApiModelProperty(value = "类型")
    private String type;
    /**
     * 是否必填
     */
    @ApiModelProperty(value = "是否必填")
    private Boolean required;
    /**
     * 提示语
     */
    @ApiModelProperty(value = " 提示语")
    private String placeholder;
}
