package com.medusa.gruul.platform.model.vo.agent;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author whh
 * @description
 * @data: 2020/11/19
 */
@Data
public class AgentDetailInfo {

    @ApiModelProperty(value = "代理id，新增的时候不需要")
    private Long id;

    @ApiModelProperty(value = "账号")
    private String account;

    @ApiModelProperty(value = "密码")
    private String pwd;

    @ApiModelProperty(value = "联系人")
    private String linkName;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "所在地区")
    private String region;

    @ApiModelProperty(value = "联系地址")
    private String address;

    @ApiModelProperty(value = "邮箱地址")
    private String email;

    @ApiModelProperty(value = "代理等级 1:代理商 2:直属渠道商 3:间接渠道商")
    private Integer agentType;

    @ApiModelProperty(value = "代理到期时间")
    private LocalDateTime nextDueTime;

    @ApiModelProperty(value = "满足代理目标到期自动续签时长 0不自动续期  1-自动续签")
    private Integer isAutoTimes;

    @ApiModelProperty(value = "代理目标")
    private List<AgentRuleVo> agentTarget;

    @ApiModelProperty(value = "升级条件")
    private List<AgentRuleVo> agentUpgrade;

    @ApiModelProperty(value = "折扣%")
    private BigDecimal discount;

    @ApiModelProperty(value = "代理商分润 单位%")
    private Integer agentShareProfit;

    @ApiModelProperty(value = "渠道商分润 单位%")
    private Integer channelShareProfit;

    @ApiModelProperty(value = "银行卡相关信息")
    private CardBankVo bankInfo;
}
