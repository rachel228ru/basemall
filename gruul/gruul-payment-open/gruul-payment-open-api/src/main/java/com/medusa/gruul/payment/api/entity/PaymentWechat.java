package com.medusa.gruul.payment.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author whh
 * @since 2019-11-04
 */
@Data
@Accessors(chain = true)
@TableName("t_payment_wechat")
@ApiModel(value = "PaymentWechat对象", description = "")
public class PaymentWechat {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 交易类型 1-JSAPI支付（或小程序支付）、2-Native支付、3-app支付，4-H5支付，
     */
    @ApiModelProperty(value = "交易类型 1-JSAPI支付（或小程序支付）、2-Native支付、3-app支付，4-H5支付，6")
    @TableField("trade_type")
    private Integer tradeType;

    /**
     * 主记录表id
     */
    @ApiModelProperty(value = "主记录表id")
    @TableField("payment_id")
    private Long paymentId;

    /**
     * 业务订单号
     */
    @ApiModelProperty(value = "业务订单号")
    @TableField("out_trade_no")
    private String outTradeNo;

    /**
     * 商品的标题/交易标题/订单标题/订单关键字等。
     */
    @ApiModelProperty(value = "商品的标题/交易标题/订单标题/订单关键字等。")
    @TableField("subject")
    private String subject;

    /**
     * 商户标识
     */
    @ApiModelProperty(value = "商户标识")
    @TableField("tenant_id")
    private String tenantId;

    /**
     * 用户openId
     */
    @ApiModelProperty(value = "用户openId")
    @TableField("open_id")
    private String openId;


}
