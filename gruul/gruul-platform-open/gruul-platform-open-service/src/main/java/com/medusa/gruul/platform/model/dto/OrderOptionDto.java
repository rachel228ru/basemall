package com.medusa.gruul.platform.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * @author whh
 * @description
 * @data: 2020/8/15
 */
@Data
public class OrderOptionDto {

    @ApiModelProperty("订单id")
    @NotNull(message = "订单id为空")
    private Long orderId;

    @ApiModelProperty("操作类型 1-收到汇款 2-关闭订单")
    @Range(max = 2, min = 1, message = "操作类型错误")
    @NotNull(message = "订单id为空")
    private Integer optionType;
}
