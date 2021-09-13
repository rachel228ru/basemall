package com.medusa.gruul.payment.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.medusa.gruul.common.data.base.BaseNoTenantEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author whh
 * @since 2019-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_payment")
@ApiModel(value = "Payment对象", description = "")
public class Payment extends BaseNoTenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 该笔订单允许的最晚付款时间，逾期将关闭交易。
     */
    @ApiModelProperty(value = "该笔订单允许的最晚付款时间，逾期将关闭交易。")
    @TableField("timeout_express")
    private LocalDateTime timeoutExpress;

    /**
     * 支付渠道:1-微信
     */
    @ApiModelProperty(value = "支付渠道:1-微信")
    @TableField("pay_channel")
    private Integer payChannel;

    /**
     * 货币类型 CNY：人民币,符合ISO 4217标准的三位字母代码
     */
    @ApiModelProperty(value = "货币类型 CNY：人民币,符合ISO 4217标准的三位字母代码(枚举)")
    @TableField("fee_type")
    private String feeType;

    /**
     * 订单总金额，单位为元，精确到小数点后两位
     */
    @ApiModelProperty(value = "订单总金额，单位为元，精确到小数点后两位")
    @TableField("total_fee")
    private BigDecimal totalFee;

    /**
     * 对一笔交易的具体描述信息
     */
    @ApiModelProperty(value = "对一笔交易的具体描述信息")
    @TableField("body")
    private String body;

    /**
     * 附加数据,格式为json字符串,怎么发送怎么返回
     */
    @ApiModelProperty(value = "附加数据,格式为json字符串,怎么发送怎么返回")
    @TableField("business_params")
    private String businessParams;

    /**
     * 终端ip
     */
    @ApiModelProperty(value = "终端ip")
    @TableField("terminal_ip")
    private String terminalIp;

    /**
     * 支付流水号
     */
    @ApiModelProperty(value = "支付流水号")
    @TableField("transaction_id")
    private Long transactionId;

    /**
     * 业务回调地址
     */
    @ApiModelProperty(value = "业务回调地址")
    @TableField("business_notify_url")
    private String businessNotifyUrl;

    /**
     * 路由键
     */
    @ApiModelProperty(value = "路由键,路由键和回调url必须选一个")
    @TableField("route_key")
    private String routeKey;

    /**
     * 第三方回调是否已处理 0-未处理 1-已处理
     */
    @ApiModelProperty(value = "第三方回调是否已处理 0-未处理 1-已处理")
    @TableField("third_party_notify_status")
    private Integer thirdPartyNotifyStatus;

    /**
     * 业务方是否已正确处理  0-未处理 1-已处理
     */
    @ApiModelProperty(value = "业务方是否已正确处理  0-未处理 1-已处理")
    @TableField("business_notify_status")
    private Integer businessNotifyStatus;

    /**
     * 交易状态：1（交易创建，等待买家付款）、2（未付款交易超时关闭）、3（交易支付成功）
     */
    @ApiModelProperty(value = "交易状态：1（交易创建，等待买家付款）、2（未付款交易超时关闭）、3（交易支付成功）")
    @TableField("trade_status")
    private Integer tradeStatus;

    /**
     * 回调次数
     */
    @ApiModelProperty(value = "回调次数")
    @TableField("third_party_notify_number")
    private Integer thirdPartyNotifyNumber;


    /**
     * 租户标识
     */
    @ApiModelProperty(value = "租户标识")
    @TableField("tenant_id")
    private String tenantId;


}
