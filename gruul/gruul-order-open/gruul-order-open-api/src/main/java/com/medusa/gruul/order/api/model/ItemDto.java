package com.medusa.gruul.order.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 商品规格数量Map
 *
 * @author alan
 * @date 2019/10/4 14:36
 */
@Data
@ApiModel(value = "商品规格数量Map", description = "商品规格数量Map")
public class ItemDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull
    @ApiModelProperty("商品SKU ID")
    private Long skuId;

    @NotNull
    @ApiModelProperty("商品数量")
    private Integer number;


}
