package com.medusa.gruul.order.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel("查询对象")
public class GetOrderListDtoByTimeScope {
    @ApiModelProperty(name = "开始时间", dataType = "yyyy-MM-dd HH:mm:ss")
    private String start;
    @ApiModelProperty(name = "结束时间 ", dataType = "yyyy-MM-dd HH:mm:ss")
    private String end;
    @ApiModelProperty(name = "租户 id ", dataType = "java.lang.String")
    private String tenantId;
    @ApiModelProperty(name = "城市合伙人/商铺 id ", dataType = "java.lang.String")
    private String shopId;

    public GetOrderListDtoByTimeScope(String start, String end, String tenantId, String shopId) {
        this.start = start;
        this.end = end;
        this.tenantId = tenantId;
        this.shopId = shopId;
    }
}