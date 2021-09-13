package com.medusa.gruul.order.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * The type Api order evaluate dto.
 * <p>
 * 订单评价
 *
 * @author alan
 * @date 2019 /11/25 21:41
 */
@Data
public class ApiOrderEvaluateDto {

    @NotNull
    @ApiModelProperty(value = "订单ID", example = "1")
    private Long orderId;

    @NotEmpty
    @ApiModelProperty("订单包含的商品")
    private List<ApiOrderProductEvaluateDto> productEvaluateDtoList;

    @NotNull
    @ApiModelProperty("商城模式下店铺评分")
    private Integer shopRate;
}
