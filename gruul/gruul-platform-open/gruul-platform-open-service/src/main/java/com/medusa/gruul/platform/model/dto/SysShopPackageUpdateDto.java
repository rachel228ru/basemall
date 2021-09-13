package com.medusa.gruul.platform.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author whh
 * @description
 * @data: 2020/8/8
 */
@Data
public class SysShopPackageUpdateDto {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @NotNull(message = "id为空")
    private Long id;

    /**
     * 套餐名称
     */
    @ApiModelProperty(value = "套餐名称")
    private String name;

    /**
     * 套餐说明
     */
    @ApiModelProperty(value = "套餐说明")
    private String remark;

    /**
     * 套餐价格
     */
    @ApiModelProperty(value = "套餐价格")
    private BigDecimal packagePrice;

    /**
     * 套餐使用价格单位 1天，2月，3年
     */
    @ApiModelProperty(value = "套餐使用价格单位 1天，2月，3年")
    private BigDecimal packagePriceUnit;

    /**
     * 优惠价json y-年 m-月 d-天  [{"unit":"y","price":100}]
     */
    @ApiModelProperty(value = "优惠价json unit=(y-年 m-月 d-天)   [{\\\"unit\\\":\\\"y\\\",\\\"price\\\":100}]")
    private String discountsJson;

    /**
     * 功能描述
     */
    @ApiModelProperty(value = "功能描述")
    private String functionDesc;

    /**
     * 套餐开启状态 0-关闭  1-开启
     */
    @ApiModelProperty(value = "套餐开启状态 0-关闭  1-开启")
    private Integer openState;

    @ApiModelProperty(value = "操作类型 1-套餐信息 2-套餐状态")
    @NotNull(message = "操作类型为空")
    @Range(min = 1, max = 2, message = "操作类型错误")
    private Integer optionType;

}
