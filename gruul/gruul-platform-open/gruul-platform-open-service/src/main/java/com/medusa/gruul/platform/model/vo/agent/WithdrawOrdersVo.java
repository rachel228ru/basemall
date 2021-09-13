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
 * @data: 2020/11/15
 */
@Data
public class WithdrawOrdersVo {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "提现方式  1-微信  2-支付宝  3-银行卡")
    private Integer type;

    @ApiModelProperty(value = "订单号")
    private String orderNum;

    @ApiModelProperty(value = "提现姓名")
    private String name;

    @ApiModelProperty(value = "开户行名称")
    private String bankName;

    @ApiModelProperty(value = "银行卡号或者支付宝微信账号")
    private String bankNum;

    @ApiModelProperty(value = "提现金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "0:待审核 1:审核拒绝 2:已打款")
    private Integer status;

    @ApiModelProperty(value = "代理联系人姓名")
    private String linkName;

    @ApiModelProperty(value = "代理账号账户")
    private String account;

    @ApiModelProperty(value = "代理级别 1代理商2直属渠道商3间接渠道商")
    private Integer agentType;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

}
