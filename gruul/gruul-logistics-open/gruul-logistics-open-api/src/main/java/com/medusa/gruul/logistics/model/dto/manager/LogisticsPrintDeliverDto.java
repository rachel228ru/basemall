package com.medusa.gruul.logistics.model.dto.manager;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author zhaozheng
 */
@Data
@ApiModel("打印发货")
public class LogisticsPrintDeliverDto {

    @ApiModelProperty("订单id")
    private List<Long> orderIds;


    @ApiModelProperty("店铺id")
    private String shopId;

    @ApiModelProperty("物流公司编号")
    @NotBlank(message = "物流公司不能为空")
    private String deliverCode;

    @ApiModelProperty("打印机编号")
    @NotBlank(message = "打印机编号不能为空")
    private String printCode;

    @ApiModelProperty("物流公司发货地址设置id")
    @NotNull(message = "物流公司发货地址不能为空")
    private Long expressId;

    @ApiModelProperty("租户id")
    private String tenantId;


}
