package com.medusa.gruul.platform.api.entity;

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
 * 充值订单表
 * </p>
 *
 * @author whh
 * @since 2020-08-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_platform_account_recharge")
@ApiModel(value = "PlatformAccountRecharge对象", description = "充值订单表")
public class PlatformAccountRecharge extends BaseNoTenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商户id
     */
    @ApiModelProperty(value = "商户id")
    @TableField("account_id")
    private Long accountId;

    /**
     * 充值编号
     */
    @ApiModelProperty(value = "充值编号")
    @TableField("recharge_num")
    private String rechargeNum;

    /**
     * 支付单号
     */
    @ApiModelProperty(value = "支付单号")
    @TableField("pay_num")
    private String payNum;

    /**
     * 订单生成时所在的店铺
     */
    @ApiModelProperty(value = "租户ID")
    private String tenantId;

    /**
     * 1支付宝 2 微信 3汇款支付
     */
    @ApiModelProperty(value = "1支付宝 2 微信 3汇款支付 （11-14为代理支付 11-支付宝支付  12-微信支付  13-汇款支付  14-余额支付）")
    @TableField("pay_type")
    private Integer payType;

    /**
     * 结束时间(异步回调时间)
     */
    @ApiModelProperty(value = "结束时间(异步回调时间)")
    @TableField("finish_time")
    private LocalDateTime finishTime;
    /**
     * 审核时间
     */
    @ApiModelProperty(value = "审核时间")
    @TableField("audit_time")
    private LocalDateTime auditTime;

    /**
     * 支付余额
     */
    @ApiModelProperty(value = "支付余额")
    @TableField("pay_amount")
    private BigDecimal payAmount;

    /**
     * 付款方信息(json) 线下打款使用
     */
    @ApiModelProperty(value = "付款方信息(json) 线下打款使用  {\"name\":\"姓名\",\"paymentTime\":\"付款时间\",\"account\":\"付款账号\"}")
    @TableField("pay_info")
    private String payInfo;

    /**
     * 支付后账户余额
     */
    @ApiModelProperty(value = "支付后账户余额")
    @TableField("account_amount")
    private BigDecimal accountAmount;

    /**
     * 充值状态: 0:生成订单 1:充值中 2:充值成功
     */
    @ApiModelProperty(value = "充值状态: 0:生成订单 1:充值中 2:充值成功 3-已关闭")
    @TableField("status")
    private Integer status;

    /**
     * 充值源头 1.商户  2.代理商 3.渠道商
     */
    @ApiModelProperty(value = "充值源头 1.商户  2.代理商 3.渠道商")
    @TableField("pay_source")
    private Integer paySource;


    /**
     * 三方预交易标识
     */
    @ApiModelProperty(value = "三方预交易标识,如果充值源头是代理商或渠道则是代理的充值订单编号")
    @TableField("prepay_id")
    private String prepayId;


    /**
     * 开票状态
     */
    @ApiModelProperty(value = "开票状态  0-未开票 1-开票中 2-已开票")
    @TableField("invoice_status")
    private Integer invoiceStatus;

}
