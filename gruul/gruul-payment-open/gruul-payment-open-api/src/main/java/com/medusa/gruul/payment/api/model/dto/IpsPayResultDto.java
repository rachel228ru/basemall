package com.medusa.gruul.payment.api.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * ips返回结果
 *
 * @author zq
 * @description
 * @data: 2020/11/30
 */
@Data
public class IpsPayResultDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "签名")
    private String sign;

    @ApiModelProperty(value = "响应码")
    private String respCode;

    @ApiModelProperty(value = "描述")
    private String respMsg;

    @ApiModelProperty(value = "业务域")
    private String data;

    @Data
    public class PayDataDto {
        @ApiModelProperty(value = "账户号")
        private String account;
        @ApiModelProperty(value = "场景类型")
        private String sceneType;
        @ApiModelProperty(value = "商户订单号")
        private String trxId;
        @ApiModelProperty(value = "订单日期")
        private String trxDtTm;
        @ApiModelProperty(value = "订单金额")
        private String trxAmt;
        @ApiModelProperty(value = "二维码链接")
        private String qrCode;
        @ApiModelProperty(value = "支付跳转链接")
        private String payUrl;
        @ApiModelProperty(value = "调用数据")
        private String payInfo;
        @ApiModelProperty(value = "备注")
        private String remark;
        @ApiModelProperty(value = "商户附加数据")
        private String attach;
        @ApiModelProperty(value = "ips订单号")
        private String ipsTrxId;
        @ApiModelProperty(value = "ips完成时间")
        private String ipsTrxDtTm;
        @ApiModelProperty(value = "三方流水号")
        private String thdTrxId;

        @Data
        public class PayInfo {
            @ApiModelProperty(value = "应用id")
            private String appId;
            @ApiModelProperty(value = "机构号")
            private String partnerId;
            @ApiModelProperty(value = "预支付回话id")
            private String prepayId;
            @ApiModelProperty(value = "订单详情扩展 : 源属性 package 关键字, 从json包 手动赋值 ")
            private String packageStr;
            @ApiModelProperty(value = "随机字符串")
            private String nonceStr;
            @ApiModelProperty(value = "时间戳")
            private String timeStamp;
            @ApiModelProperty(value = "签名")
            private String paySign;
            @ApiModelProperty(value = "签名类型")
            private String signType;
            @ApiModelProperty(value = "交易流水号")
            private String tradeNO;
            @ApiModelProperty(value = "状态")
            private String status;
        }
    }

}
