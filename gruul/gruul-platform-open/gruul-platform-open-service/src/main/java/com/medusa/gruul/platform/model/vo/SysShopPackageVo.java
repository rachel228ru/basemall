package com.medusa.gruul.platform.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author whh
 * @description
 * @data: 2020/8/8
 */
@Data
public class SysShopPackageVo {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
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
     * 套餐等级
     */
    @ApiModelProperty(value = "套餐等级")
    private Integer level;

    /**
     * 套餐价格
     */
    @ApiModelProperty(value = "套餐价格")
    private BigDecimal packagePrice;

    /**
     * 套餐使用价格单位 1天，2月，3年
     */
    @ApiModelProperty(value = "套餐使用价格单位 1天，2月，3年")
    private Integer packagePriceUnit;

    /**
     * 优惠价json y-年 m-月 d-天  [{"unit":"y","price":100}]
     */
    @ApiModelProperty(value = "优惠价json  unit=y-年 m-月 d-天,value =单位值,price优惠价 [{\\\"unit\\\":\\\"y\\\",\\\"value\\\":365,\\\"price\\\":100}]")
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

}
