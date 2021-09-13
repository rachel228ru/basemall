package com.medusa.gruul.logistics.model.dto.manager;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CountCostDto {
    /**
     * 解释
     */
    @ApiModelProperty("解释")
    private String des;
    /**
     * 运费金额 单位 :元
     */
    @ApiModelProperty("运费金额 单位 :元")
    private BigDecimal cost;

}
