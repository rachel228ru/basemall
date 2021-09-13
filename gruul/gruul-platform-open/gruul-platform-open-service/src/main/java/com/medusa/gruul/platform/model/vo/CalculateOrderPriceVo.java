package com.medusa.gruul.platform.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author whh
 * @description
 * @data: 2020/9/4
 */
@Data
public class CalculateOrderPriceVo {

    @ApiModelProperty(value = "优惠价格")
    private BigDecimal preferentialPrice;
    @ApiModelProperty(value = "实际价格")
    private BigDecimal actualPrice;
    @ApiModelProperty(value = "代理套餐价格")
    private BigDecimal agentPackagePrice;
    @ApiModelProperty(value = "代理实际价格")
    private BigDecimal agentPrice;
}
