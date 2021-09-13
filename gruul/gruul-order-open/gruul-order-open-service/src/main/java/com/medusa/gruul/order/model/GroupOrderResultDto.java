package com.medusa.gruul.order.model;

import com.medusa.gruul.order.api.model.ItemDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type Group order result dto.
 * <p>
 * 查询订单需要的参数
 *
 * @author alan
 * @date 2019 /11/5 21:59
 */
@Data
@ApiModel(value = "查询订单需要的参数")
public class GroupOrderResultDto {

    @NotNull
    @ApiModelProperty(value = "之前返回的Order ID", example = "1")
    private Long orderId;
    @NotEmpty
    @ApiModelProperty(value = "SKU ID")
    private List<ItemDto> items;

    /**
     * Gets sku id set.
     *
     * @return the sku id set
     */
    @ApiIgnore
    public Set<Long> getSkuIdSet() {
        return items.stream().map(ItemDto::getSkuId).collect(Collectors.toSet());
    }
}
