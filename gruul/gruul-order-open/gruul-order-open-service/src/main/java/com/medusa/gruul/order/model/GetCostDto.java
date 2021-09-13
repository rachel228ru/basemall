package com.medusa.gruul.order.model;

import com.medusa.gruul.order.api.model.ItemDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Get cost dto.
 */
@Data
public class GetCostDto {
    @ApiModelProperty("总金额")
    private BigDecimal amount;
    @ApiModelProperty("经度,120.21937542")
    private double longitude;
    @ApiModelProperty("纬度 30.25924446")
    private double latitude;
    @ApiModelProperty("收货地址ID")
    private Long addressId;
    @NotNull
    @ApiModelProperty("配送类型：0-->用户自提（需要经纬度、提货点ID、总金额）;1->物流模式（需要商品规格数量Map、收货地址中post_code）;2->送货上门（需要经纬度、提货点ID、总金额）")
    private Integer type;
    @ApiModelProperty("商品规格数量Map")
    private List<ItemDto> items;
    @ApiModelProperty("区域编码")
    private String region;


    /**
     * Gets item sku ids.
     *
     * @return the item sku ids
     */
    @ApiIgnore
    public List<Long> getItemSkuIds() {
        return new ArrayList<>(items.stream().map(ItemDto::getSkuId).collect(Collectors.toSet()));
    }
}
