package com.medusa.gruul.platform.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author whh
 * @description
 * @data: 2020/8/2
 */
@Data
public class PayRecordVo {

    @ApiModelProperty(value = "充值编号")
    private String rechargeNum;


    @ApiModelProperty(value = "1支付宝 2 微信 3汇款支付")
    private Integer payType;

    @ApiModelProperty(value = "支付余额")
    private BigDecimal payAmount;


    @ApiModelProperty(value = "支付后账户余额")
    private BigDecimal accountAmount;

    @ApiModelProperty(value = "充值状态: 0:生成订单 1:充值中 2:充值成功 ")
    private Integer status;

    @ApiModelProperty(value = "充值平台 1.商户  2.代理商 3.渠道商")
    private Integer paySource;

    @ApiModelProperty("充值时间")
    private LocalDateTime createTime;
}
