package com.medusa.gruul.payment.api.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.medusa.gruul.common.data.base.BaseEntity;
import com.medusa.gruul.payment.api.enums.CheckNameEnum;
import com.medusa.gruul.payment.api.enums.PayChannelEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * @author create by zq
 * @date created in 2020/03/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_ent_pay")
@ApiModel(value = "EntPay对象", description = "EntPay实体")
public class EntPay extends BaseEntity {


    private static final long serialVersionUID = 1L;


    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 支付渠道:1-微信
     */
    @ApiModelProperty(value = "支付渠道:1-微信")
    @TableField("pay_channel")
    private PayChannelEnum payChannel;


    /**
     * 支付渠道类型
     */
    @ApiModelProperty(value = "支付渠道类型")
    @TableField("pay_channel_type")
    private PayChannelEnum payChannelType;


    /**
     * 订单总金额，单位为分
     */
    @ApiModelProperty(value = "订单总金额，单位为分")
    @TableField("amount")
    private Integer amount;


    /**
     * 企业付款备注
     */
    @ApiModelProperty(value = "企业付款备注")
    @TableField("description")
    private String description;


    /**
     * 附加数据,格式为json字符串,怎么发送怎么返回
     */
    @ApiModelProperty(value = "附加数据,格式为json字符串,怎么发送怎么返回")
    @TableField("business_params")
    private String businessParams;


    /**
     * 支付流水号
     */
    @ApiModelProperty(value = "支付流水号")
    @TableField("transaction_id")
    private String transactionId;


    /**
     * 交易状态：1、 发起支付 2、 交易支付成功 3、交易支付失败 4、交易同步返回成功单业务未成功
     */
    @ApiModelProperty(value = "交易状态：1、 发起支付 2、 交易支付成功 3、交易支付失败 4、交易同步返回成功单业务未成功")
    @TableField("trade_status")
    private String tradeStatus;


    /**
     * 用户标识
     */
    @ApiModelProperty(value = "用户标识")
    @TableField("openid")
    private String openid;


    /**
     * 校验用户姓名选项
     */
    @ApiModelProperty(value = "校验用户姓名选项")
    @TableField("check_name")
    private CheckNameEnum checkName;


    /**
     * 收款用户姓名. 可选
     */
    @ApiModelProperty(value = "收款用户姓名.")
    @TableField("re_user_name")
    private String reUserName;


    /**
     * 调用接口的机器Ip地址
     */
    @ApiModelProperty(value = "调用接口的机器Ip地址")
    @TableField("spbill_create_ip")
    private String spbillCreateIp;


    /**
     * 支付成功时间
     */
    @ApiModelProperty(value = "支付成功时间")
    @TableField("pay_time")
    private String payTime;

}
