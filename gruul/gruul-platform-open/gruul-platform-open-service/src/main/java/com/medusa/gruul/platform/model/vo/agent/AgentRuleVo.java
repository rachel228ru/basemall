package com.medusa.gruul.platform.model.vo.agent;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * @author whh
 * @description
 * @data: 2020/10/31
 */
@Data
public class AgentRuleVo {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 代理目标  1.销售总单数 2.销售总额
     */
    @ApiModelProperty(value = "代理目标  1.销售总单数 2.销售总额")
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
