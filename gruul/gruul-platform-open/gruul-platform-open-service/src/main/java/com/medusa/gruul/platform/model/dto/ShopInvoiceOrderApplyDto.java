package com.medusa.gruul.platform.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * @author whh
 * @description
 * @data: 2020/8/16
 */
@Data
public class ShopInvoiceOrderApplyDto {

    @ApiModelProperty(value = "发票类型：1增值税专用发票、2普通发票 3专业发票")
    @Range(min = 1, max = 3, message = "发票类型错误")
    @Range(max = 2, min = 1, message = "订单类型错误")
    @NotNull(message = "发票类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "订购订单id")
    @NotNull(message = "订单不能为空")
    private Long orderId;


    @ApiModelProperty(value = "用户发票抬头id")
    @NotNull(message = "用户发票抬头id不能为空")
    private Long invoiceRiseId;

    @ApiModelProperty(value = "订单类型 1-充值订单  2-套餐订购订单")
    @NotNull(message = "订单类型错误")
    @Range(max = 2, min = 1, message = "订单类型错误")
    private Integer orderType;


}
