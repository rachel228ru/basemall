package com.medusa.gruul.order.model;

import com.medusa.gruul.common.core.param.QueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * The type Incentive detail dto.
 * <p>
 * 查询激励佣金详情
 *
 * @author alan
 * @date 2019 /11/10 9:21
 */
@Data
@ApiModel(value = "查询激励佣金详情")
public class IncentiveDetailDto extends QueryParam {


    @NotBlank
    @ApiModelProperty(value = "查询的月份")
    private String month;

    @NotNull
    @ApiModelProperty(value = "账户类型枚举")
    private String accountType;

    @NotBlank
    @ApiModelProperty(value = "对应的账户ID")
    private String userId;

    @ApiModelProperty(value = "配送方式 0->全部;100->社区订单;102->快递订单")
    private Integer deliverType;
}
