package com.medusa.gruul.afs.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * The type Seller refusal dto.
 *
 * @author alan
 * @description: SellerRefusalDto.java
 * @date 2020 /8/5 22:47
 */
@Data
@ApiModel(value = "商家拒绝的参数")
public class SellerRefusalDto {

    @NotNull
    @ApiModelProperty(value = "售后订单id")
    private Long afsId;

    @NotBlank
    @ApiModelProperty(value = "商家拒绝原因")
    private String refusalReason;
}
