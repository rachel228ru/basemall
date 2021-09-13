package com.medusa.gruul.logistics.model.dto.manager;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 聚合统计
 * @author lcy
 */
@Data
@ApiModel("物流聚合统计")
public class LogisticsAggregationDto {

    @ApiModelProperty("模版id")
    private Long freightTemplateId;

    @ApiModelProperty("总金额")
    private BigDecimal  priceSum;

    @ApiModelProperty("总件数")
    private Integer numSum;

    @ApiModelProperty("总重量")
    private BigDecimal  weightSum;



}
