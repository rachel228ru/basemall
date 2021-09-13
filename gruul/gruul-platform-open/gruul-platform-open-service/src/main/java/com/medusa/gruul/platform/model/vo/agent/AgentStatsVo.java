package com.medusa.gruul.platform.model.vo.agent;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author whh
 * @description
 * @data: 2020/11/14
 */
@Data
public class AgentStatsVo {


    @ApiModelProperty(value = "可用金额")
    private BigDecimal availableAmount;

    @ApiModelProperty(value = "冻结金额")
    private BigDecimal freezeAmount;

    @ApiModelProperty(value = "已经分帐金额")
    private BigDecimal subLedgerAmount;

    @ApiModelProperty(value = "1:代理商 2:直属渠道商 3:间接渠道商")
    private Integer type;

    @ApiModelProperty(value = "渠道商总数 type ==1 返回")
    private Integer channalCount;

    @ApiModelProperty(value = "直接商户总数")
    private Integer directMerchantCount;

    @ApiModelProperty(value = "间接商户总数 type ==1 返回")
    private Integer inDirectMerchantCount;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}
