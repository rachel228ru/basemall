package com.medusa.gruul.payment.api.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 * @date 2019/11/06
 */
@Data
public class WxPayResultDto {

    @ApiModelProperty(value = "小程序Id")
    private String appId;
    @ApiModelProperty(value = "时间戳")
    private String timeStamp;
    @ApiModelProperty(value = "随机字符串")
    private String nonceStr;
    @ApiModelProperty(value = "数据包")
    private String packageValue;
    @ApiModelProperty(value = "签名方式")
    private String signType;
    @ApiModelProperty(value = "签名")
    private String paySign;
    @ApiModelProperty(value = "业务结果 SUCCESS/FAIL")
    private String resultCode;
    @ApiModelProperty(value = "错误代码")
    private String errCode;
    @ApiModelProperty(value = "错误代码描述")
    private String errCodeDes;
    @ApiModelProperty(value = "交易类型-JSAPI，return_code 和result_code都为SUCCESS的时候有返回")
    private String tradeType;
    @ApiModelProperty(value = "预支付交易会话标识，return_code 和result_code都为SUCCESS的时候有返回")
    private String codeUrl;
    @ApiModelProperty(value = "二维码链接 weixin://wxpay/bizpayurl/up?pr=NwY5Mz9&groupid=00，return_code 和result_code都为SUCCESS的时候有返回")
    private String prepayId;
    @ApiModelProperty(value = "支付流水号")
    private String transactionId;
    @ApiModelProperty(value = "mWebUrl")
    private String mWebUrl;
}
