package com.medusa.gruul.payment.api.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 微信回调结果
 *
 * @author whh
 * @description
 * @data: 2019/11/30
 */
@Data
public class WxPayNotifyResultDto  implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "业务订单号")
    private String outTradeNo;
    @ApiModelProperty(value = "附加数据,格式为json字符串,怎么发送怎么返回")
    private String businessParams;
    @ApiModelProperty(value = "租户标识")
    private String tenantId;
    @ApiModelProperty(value = "支付流水号")
    private String transactionId;

    public WxPayNotifyResultDto() {
    }

    public WxPayNotifyResultDto(String outTradeNo, String businessParams, String tenantId, String transactionId) {
        this.outTradeNo = outTradeNo;
        this.businessParams = businessParams;
        this.tenantId = tenantId;
        this.transactionId = transactionId;
    }
}
