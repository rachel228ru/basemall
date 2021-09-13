package com.medusa.gruul.payment.api.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * @author create by zq
 * @date created in 2020/03/13
 */
@Data
public class RefundNotifyResultDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "业务订单号")
    private String outTradeNo;
    @ApiModelProperty(value = "附加数据,格式为json字符串,怎么发送怎么返回")
    private String businessParams;
    @ApiModelProperty(value = "租户标识")
    private String tenantId;
    @ApiModelProperty(value = "支付流水号")
    private String transactionId;

    /**
     * 商户退款单号.
     */
    @ApiModelProperty(value = "商户退款单号")
    private String outRefundNo;

    /**
     * 微信退款单号.
     */
    @ApiModelProperty(value = "微信退款单号")
    private String refundId;

    /**
     * 退款金额.
     */
    @ApiModelProperty(value = "退款金额")
    private Integer refundFee;

    /**
     * 应结退款金额.
     */
    @ApiModelProperty(value = "应结退款金额")
    private Integer settlementRefundFee;

    /**
     * 返回状态码.
     */
    @ApiModelProperty(value = "返回状态码")
    protected String returnCode;

    /**
     * 返回信息.
     */
    @ApiModelProperty(value = "返回信息")
    protected String returnMsg;

    /**
     * 业务结果.
     */
    @ApiModelProperty(value = "业务结果")
    private String resultCode;

    /**
     * 错误代码.
     */
    @ApiModelProperty(value = "错误代码")
    private String errCode;

    /**
     * 错误代码描述.
     */
    @ApiModelProperty(value = "错误代码描述")
    private String errCodeDes;

}
