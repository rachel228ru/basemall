package com.medusa.gruul.platform.model.dto.agent;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * @author whh
 * @description
 * @data: 2020/10/18
 */
@Data
public class AgentRuleDto {

    /**
     * 代理目标  1.销售总单数 2.销售总额
     */
    @ApiModelProperty(value = "代理目标  1.销售总单数 2.销售总额")
    @Range(min = 1, max = 2, message = "代理目标类型错误")
    private Integer ruleType;

    /**
     * 完成目标时间节点,单位月
     */
    @ApiModelProperty(value = "完成目标时间节点,单位月")
    private Integer targetTime;

    /**
     * 完成目标所需值
     */
    @ApiModelProperty(value = "完成目标所需值")
    private String targetValue;

    /**
     * 是否使用规则 0-未使用 1-已使用
     */
    @ApiModelProperty(value = "是否使用规则 0-未使用 1-已使用")
    private Integer useRule;

}
