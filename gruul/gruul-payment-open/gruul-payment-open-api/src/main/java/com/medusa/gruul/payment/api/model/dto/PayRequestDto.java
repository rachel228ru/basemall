package com.medusa.gruul.payment.api.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author whh
 * @date 2019/11/06
 */
@Data
public class PayRequestDto {

    @ApiModelProperty(value = "商户标识 ,必要参数")
    private String tenantId;
    @ApiModelProperty(value = "支付渠道 1-微信支付(枚举) ,必要参数")
    private Integer payChannel;
    @ApiModelProperty(value = "交易类型 1--JSAPI支付（小程序appId支付）、2--Native支付、3--app支付，4--JSAPI支付（公众号appId支付）   6-H5支付 ,必要参数")
    private Integer tradeType;
    @ApiModelProperty(value = "商户订单号 ,必要参数")
    private String outTradeNo;
    @ApiModelProperty(value = "路由键,路由键和回调url必须选一个")
    private String routeKey;
    @ApiModelProperty(value = "异步接收支付结果通知的回调地址,回调业务处理成功之后需返回success未返回则会根据第三方回调次数进行多次返回,队列名和回调参数必须选一个")
    private String notifyUrl;
    @ApiModelProperty(value = "trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。")
    private String openId;
    @ApiModelProperty(value = "交易总金额，单位为元，精确到小数点后两位 ,必要参数")
    private BigDecimal totalFee;
    @ApiModelProperty(value = "终端ip")
    private String terminalIp;

    @ApiModelProperty(value = "货币类型 CNY：人民币(默认使用人民币作为单位)")
    private String feeType;
    @ApiModelProperty(value = "该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天， 该参数数值不接受小数点。(默认采用15m)")
    private String timeoutExpress;
    @ApiModelProperty(value = "对一笔交易的具体描述信息")
    private String body;
    @ApiModelProperty(value = "附加数据,格式为json字符串,怎么发送怎么返回")
    private String businessParams;
    @ApiModelProperty(value = "商品的标题/交易标题/订单标题/订单关键字等。")
    private String subject;


}
