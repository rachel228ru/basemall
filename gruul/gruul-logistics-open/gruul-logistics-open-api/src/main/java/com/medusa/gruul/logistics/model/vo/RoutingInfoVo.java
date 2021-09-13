package com.medusa.gruul.logistics.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *  路由及运单号信息
 * </p>
 *
 * @author lcysike
 * @since 2020-06-08
 */
@Data
@ApiModel("路由及运单号信息")
public class RoutingInfoVo {

    @ApiModelProperty("快递运单号")
    private String waybillCode;

    @ApiModelProperty("集包地信息")
    private String packageName;

    @ApiModelProperty("集包地信息")
    private String packageCode;

    @ApiModelProperty("分拣码")
    private String sortingCode;
}
