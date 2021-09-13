package com.medusa.gruul.logistics.model.dto.manager;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zhaozheng
 */
@Data
@ApiModel("批量发货")
public class LogisticsDeliverDto {

    @ApiModelProperty("物流公司编号")
    private List<LogisticsBatchDeliverDto> logisticsBatchDeliverDtos;


}
