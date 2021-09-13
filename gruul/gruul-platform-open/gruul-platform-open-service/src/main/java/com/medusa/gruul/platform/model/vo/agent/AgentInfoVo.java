package com.medusa.gruul.platform.model.vo.agent;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author whh
 * @description 代理信息
 * @data: 2020/11/1
 */
@Data
public class AgentInfoVo {

    @ApiModelProperty(value = "联系人")
    private String linkName;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "邀请码")
    private String code;

    @ApiModelProperty(value = "1:代理商 2:直属渠道商 3:间接渠道商")
    private Integer type;

    @ApiModelProperty(value = "可用金额")
    private BigDecimal availableAmount;

    @ApiModelProperty(value = "折扣")
    private BigDecimal discount;

    @ApiModelProperty(value = "满足代理目标到期自动续签时长 0不自动续期  1-自动续签")
    private Integer isAutoTimes;

    @ApiModelProperty(value = "代理商分润 单位%")
    private Integer agentShareProfit;

    @ApiModelProperty(value = "渠道商分润 单位%")
    private Integer channelShareProfit;

    @ApiModelProperty(value = "代理狀態 1:正常 2:冻结 3:停用")
    private Integer status;

    @ApiModelProperty(value = "代理目标")
    private List<AgentRuleVo> agentTarget;

    @ApiModelProperty(value = "升级条件")
    private List<AgentRuleVo> agentUpgrade;

    @ApiModelProperty(value = "未读消息条数")
    private Integer notRead;

    @ApiModelProperty(value = "代理到期时间")
    private LocalDateTime nextDueTime;
}
