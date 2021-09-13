package com.medusa.gruul.order.model;

import com.medusa.gruul.common.core.param.QueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * The type Query user order dto.
 * <p>
 * 查询用户订单列表参数
 *
 * @author alan
 * @date 2019 /11/13 20:54
 */
@Data
@ApiModel(value = "查询用户订单列表参数", description = "查询用户订单列表参数")
public class QueryUserOrderDto extends QueryParam {


    @NotNull
    @ApiModelProperty("要查询的订单状态：101->待发货;102->配送中;103->待提货;")
    private Integer status;

    @NotNull
    @ApiModelProperty(value = "要查询的客户ID", example = "1")
    private String userId;

}