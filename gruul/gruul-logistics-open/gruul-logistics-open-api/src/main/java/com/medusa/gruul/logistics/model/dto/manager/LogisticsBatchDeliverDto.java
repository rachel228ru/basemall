package com.medusa.gruul.logistics.model.dto.manager;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhaozheng
 */
@Data
@ApiModel("批量发货")
public class LogisticsBatchDeliverDto {

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("物流公司编号")
    private String deliverCode;

    @ApiModelProperty("物流公司名称")
    private String deliverName;

    @ApiModelProperty("物流单号")
    private String logisticsCode;



}
