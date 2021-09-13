package com.medusa.gruul.platform.model.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author whh
 * @description
 * @data: 2020/8/11
 */
@Data
public class RechargeRecordOrderVo {
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
     * 付款方信息(json) 线下打款使用
     */
    @ApiModelProperty(value = "付款方信息(json) 线下打款使用")
    private String payInfo;


    /**
     * 充值状态: 0:生成订单 1:充值中 2:充值成功
     */
    @ApiModelProperty(value = "充值状态: 0:生成订单 1:充值中 2:充值成功 ")
    private Integer status;

    /**
     * 充值源头 1.商户  2.代理商 3.渠道商
     */
    @ApiModelProperty(value = "充值源头 1.商户  2.代理商 3.渠道商")
    private Integer paySource;


    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户名称")
    private String nikeName;

    /**
     * 头像url
     */
    @ApiModelProperty(value = "头像url")
    private String avatarUrl;

    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty("充值时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "审核时间")
    private LocalDateTime auditTime;
}
