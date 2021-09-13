package com.medusa.gruul.payment.api.model.dto;

import com.medusa.gruul.payment.api.enums.CheckNameEnum;
import com.medusa.gruul.payment.api.enums.PayChannelEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author create by zq
 * @date created in 2019/11/18
 */
@Data
public class EntPayReQuestDto {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private Long id;


    /**
     * 支付渠道:1-微信
     */
    @ApiModelProperty(value = "支付渠道:1-微信")
    private PayChannelEnum payChannel;


    /**
     * 订单总金额，单位为分
     */
    @ApiModelProperty(value = "订单总金额，单位为分")
    private Integer amount;


    /**
     * 企业付款备注
     */
    @ApiModelProperty(value = "企业付款备注")
    private String description;


    /**
     * 附加数据,格式为json字符串,怎么发送怎么返回
     */
    @ApiModelProperty(value = "附加数据,格式为json字符串,怎么发送怎么返回")
    private String businessParams;


    /**
     * 调用接口的机器Ip地址
     */
    @ApiModelProperty(value = "调用接口的机器Ip地址")
    private String spbillCreateIp;


    /**
     * 支付流水号
     */
    @ApiModelProperty(value = "支付流水号")
    private String transactionId;


    /**
     * 交易状态：1、 发起支付 2、 交易支付成功 3、交易支付失败 4、交易同步返回成功单业务未成功
     */
    @ApiModelProperty(value = "交易状态：1、 发起支付 2、 交易支付成功 3、交易支付失败 4、交易同步返回成功单业务未成功")
    private Integer tradeStatus;


    /**
     * 租户标识
     */
    @ApiModelProperty(value = "租户标识")
    private String tenantId;


    /**
     * 用户标识
     */
    @ApiModelProperty(value = "用户标识")
    private String openid;


    /**
     * 校验用户姓名选项
     */
    @ApiModelProperty(value = "校验用户姓名选项")
    private CheckNameEnum checkName;


    /**
     * 收款用户姓名. 可选
     */
    @ApiModelProperty(value = "收款用户姓名.")
    private String reUserName;


    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id.")
    private String orderId;


    /**
     * 订单金额
     */
    @ApiModelProperty(value = "订单金额")
    private Integer totalFee;

}
