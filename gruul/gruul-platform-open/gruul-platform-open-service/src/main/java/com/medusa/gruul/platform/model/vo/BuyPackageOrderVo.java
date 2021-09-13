package com.medusa.gruul.platform.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 * @description
 * @data: 2020/8/18
 */
@Data
public class BuyPackageOrderVo {
    @ApiModelProperty(value = "订单id")
    private Long orderId;


    @ApiModelProperty(value = "支付字符串")
    private String codeUrl;
}
