package com.medusa.gruul.platform.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 * @description
 * @data: 2020/8/2
 */
@Data
public class BalanceRechargeVo {

    @ApiModelProperty("订单id")
    private Long orderId;


    @ApiModelProperty("二维码字符串标识,对其进行生成二维码")
    private String codeUrl;
}
