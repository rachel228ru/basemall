package com.medusa.gruul.platform.model.vo.agent;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author whh
 * @description
 * @data: 2020/11/18
 */
@Data
public class BalanceDetailsVo {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "头像url")
    private String avatarUrl;


    @ApiModelProperty(value = "用户名称")
    private String nikeName;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "充值编号")
    private String rechargeNum;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "充值金额")
    private BigDecimal payAmount;

    @ApiModelProperty(value = "1扫码支付->支付宝2扫码支付->微信 3汇款支付 4-代理余额支付")
    private Integer payType;

    @ApiModelProperty(value = "支付后账户余额")
    private BigDecimal accountAmount;

    @ApiModelProperty(value = "充值状态:  1:充值中 2:充值成功 3-已关闭")
    private Integer status;

    @ApiModelProperty(value = "付款方信息(json) 线下打款(汇款支付)使用 {\"name\":\"姓名\",\"paymentTime\":\"付款时间\",\"account\":\"付款账号\"}")
    private String payInfo;

}
