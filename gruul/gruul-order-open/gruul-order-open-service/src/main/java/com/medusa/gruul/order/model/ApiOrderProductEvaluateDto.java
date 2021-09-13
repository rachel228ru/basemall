package com.medusa.gruul.order.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * The type Api order product evaluate dto.
 * <p>
 * 订单商品评价
 *
 * @author alan
 * @date 2019 /11/25 21:59
 */
@Data
public class ApiOrderProductEvaluateDto {

    @NotNull
    @ApiModelProperty("商品评分")
    private Integer rate;
    @NotNull
    @ApiModelProperty(value = "商品ID", example = "1")
    private Long productSkuId;
    @NotBlank
    @ApiModelProperty("商品评价")
    private String comment;
    @NotBlank
    @ApiModelProperty("商品评价图片")
    private String picture;
}
