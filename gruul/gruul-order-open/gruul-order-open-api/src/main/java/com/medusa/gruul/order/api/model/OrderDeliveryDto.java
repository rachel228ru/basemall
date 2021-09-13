package com.medusa.gruul.order.api.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


/**
 * <p>
 * 物流发货
 * </p>
 *
 * @author wangpeng
 * @since 2020-03-14
 */
@Data
@ApiModel(value = "物流发货")
public class OrderDeliveryDto {

    @NotNull
    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "物流公司")
    private String deliveryCompany;


    @ApiModelProperty(value = "物流单号")
    private String deliverySn;

    @ApiModelProperty(value = "物流公司编号")
    private String deliveryCode;


    //======入库参数=

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime deliveryTime;

    @ApiModelProperty("集包地信息")
    private String packageName;
    @ApiModelProperty("集包地信息")
    private String packageCode;
    @ApiModelProperty("分拣码")
    private String sortingCode;


}