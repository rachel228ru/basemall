package com.medusa.gruul.order.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GetOrderListParam {
    private String tenantId;
    private String shopId;
    private List<Long> orderIds;

    public GetOrderListParam(String tenantId, String shopId, List<Long> orderIds) {
        this.tenantId = tenantId;
        this.shopId = shopId;
        this.orderIds = orderIds;
    }
}
