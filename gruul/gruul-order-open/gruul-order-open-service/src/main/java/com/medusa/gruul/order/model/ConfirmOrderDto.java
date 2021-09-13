package com.medusa.gruul.order.model;

import com.medusa.gruul.order.api.enums.OrderTypeEnum;
import com.medusa.gruul.order.api.model.ItemDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Confirm order dto.
 * <p>
 * 订单提交页所需参数
 *
 * @author alan
 * @date 2019 /11/5 21:42
 */
@Data
@ApiModel(value = "订单提交页所需参数")
public class ConfirmOrderDto {


    /**
     * 商品规格数量Map
     */
    @NotEmpty(message = "商品不能为空")
    @ApiModelProperty("商品规格数量Map")
    private List<ItemDto> items;


    /**
     * 订单类型
     */
    @ApiModelProperty("订单类型")
    private OrderTypeEnum type;


    /**
     * Gets item sku ids.
     *
     * @return the item sku ids
     */
    @ApiModelProperty(hidden = true)
    public List<Long> getItemSkuIds() {
        return new ArrayList<>(items.stream().map(ItemDto::getSkuId).collect(Collectors.toSet()));
    }
}
