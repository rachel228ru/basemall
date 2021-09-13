package com.medusa.gruul.platform.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author whh
 * @description
 * @data: 2020/8/15
 */
@Data
public class RechargeConsoleOrderVo {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 充值编号
     */
    @ApiModelProperty(value = "充值编号")
    private String rechargeNum;


    @ApiModelProperty(value = "1支付宝 2 微信 3汇款支付 （11-14为代理支付 11-支付宝支付  12-微信支付  13-汇款支付  14-余额支付）")
    private Integer payType;


    /**
     * 支付余额
     */
    @ApiModelProperty(value = "支付余额")
    private BigDecimal payAmount;

    /**
     * 支付前账户余额
     */
    @ApiModelProperty(value = "支付前账户余额")
    private BigDecimal accountAmount;

    /**
     * 充值状态: 0:生成订单 1:充值中 2:充值成功 3-已关闭
     */
    @ApiModelProperty(value = "充值状态: 0:生成订单 1:充值中 2:充值成功 3-已关闭")
    private Integer status;

    /**
     * 充值源头 1.商户  2.代理商 3.渠道商
     */
    @ApiModelProperty(value = "充值源头 1.商户  2.代理商 3.渠道商")
    private Integer paySource;

    @ApiModelProperty("充值时间")
    private LocalDateTime createTime;


    @ApiModelProperty(value = "开票状态  0-未开票 1-开票中 2-已开票")
    private Integer invoiceStatus;

    @ApiModelProperty(value = "开票信息")
    private InvoiceOrderApplyVo invoiceOrderApplyVo;

    @ApiModelProperty(value = "审核时间")
    private LocalDateTime auditTime;
}
