package com.medusa.gruul.platform.model.dto.agent;

import cn.hutool.core.collection.CollectionUtil;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.validator.ValidatorUtils;
import com.medusa.gruul.common.core.validator.group.AddGroup;
import com.medusa.gruul.common.core.validator.group.UpdateGroup;
import com.medusa.gruul.platform.model.dto.CardBankDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author whh
 * @description
 * @data: 2020/10/21
 */
@Data
public class AgenInfoDto {


    @ApiModelProperty(value = "代理id，新增的时候不需要")
    private Long id;

    @ApiModelProperty(value = "企业名称", hidden = true)
    private String enterprise;

    @ApiModelProperty(value = "企业类型编号", hidden = true)
    private String enterpriseType;

    @ApiModelProperty(value = "公司规模编号", hidden = true)
    private String scaleCode;

    @ApiModelProperty(value = "账号")
    @NotBlank(message = "账号不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String account;

    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String pwd;

    @ApiModelProperty(value = "联系人")
    @NotBlank(message = "联系人不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String linkName;

    @ApiModelProperty(value = "联系电话")
    @NotBlank(message = "联系电话不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String phone;

    @ApiModelProperty(value = "所在地区")
    @NotBlank(message = "所在地区不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String region;

    @ApiModelProperty(value = "联系地址")
    private String address;

    @ApiModelProperty(value = "邮箱地址")
    private String email;

    @ApiModelProperty(value = "代理等级 1:代理商 2:直属渠道商 ")
    @NotNull(message = "代理等级不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Range(min = 1, max = 2, message = "超出有效值", groups = {AddGroup.class, UpdateGroup.class})
    private Integer agentType;

    @ApiModelProperty(value = "代理到期时间")
    @NotNull(message = "代理期限不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private LocalDateTime nextDueTime;

    @ApiModelProperty(value = "满足代理目标到期自动续签时长 0不自动续期  1-自动续签")
    @Range(min = 0, max = 1, message = "自动续签异常", groups = {AddGroup.class, UpdateGroup.class})
    private Integer isAutoTimes = 0;

    @ApiModelProperty(value = "代理目标")
    private List<AgentRuleDto> agentTarget;

    @ApiModelProperty(value = "升级条件")
    private List<AgentRuleDto> agentUpgrade;


    @ApiModelProperty(value = "折扣%")
    @NotNull(message = "折扣不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Range(max = 100, min = 0, message = "折扣超出范围", groups = {AddGroup.class, UpdateGroup.class})
    private BigDecimal discount;

    @ApiModelProperty(value = "代理商分润 单位%")
    @NotNull(message = "代理商分润不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Range(max = 100, min = 0, message = "代理商分润超出范围", groups = {AddGroup.class, UpdateGroup.class})
    private Integer agentShareProfit;

    @ApiModelProperty(value = "渠道商分润 单位%")
    @NotNull(message = "渠道商分润不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Range(max = 100, min = 0, message = "渠道商分润超出范围", groups = {AddGroup.class, UpdateGroup.class})
    private Integer channelShareProfit;

    @ApiModelProperty(value = "银行卡相关信息")
    @NotNull(message = "银行卡信息不能为空")
    private @Valid CardBankDto bankInfo;

    public void validate() {
        if (isAutoTimes.equals(CommonConstants.NUMBER_ONE)) {
            if (CollectionUtil.isEmpty(agentTarget)) {
                throw new ServiceException("勾选满足代理目标到期自动续签时长之后,代理目标必须选一个");
            }
            ValidatorUtils.validateEntity(agentTarget);
            validateRuleType(agentTarget);
        }

        //代理等级 1:代理商 2:直属渠道商
        if (agentType.equals(CommonConstants.NUMBER_TWO)) {
            if (CollectionUtil.isEmpty(agentUpgrade)) {
                throw new ServiceException("渠道商的升级条件不能为空");
            }
            ValidatorUtils.validateEntity(agentUpgrade);
            validateRuleType(agentUpgrade);
        }
        //校验代理分润不能超过100%
        final int maxProfit =100;
        if (agentShareProfit + channelShareProfit > maxProfit) {
            throw new ServiceException("代理分润设置异常");
        }
    }

    private void validateRuleType(List<AgentRuleDto> agentTarget) {
        Map<Integer, List<AgentRuleDto>> agentRuleMap = agentTarget.stream().collect(Collectors.groupingBy(AgentRuleDto::getRuleType));
        List<AgentRuleDto> ruleTypeOne = agentRuleMap.get(CommonConstants.NUMBER_ONE);
        if (ruleTypeOne != null && ruleTypeOne.size() > 1) {
            throw new ServiceException("重复规则目标参数");
        }
        List<AgentRuleDto> ruleTypeTwo = agentRuleMap.get(CommonConstants.NUMBER_ONE);
        if (ruleTypeTwo != null && ruleTypeTwo.size() > 1) {
            throw new ServiceException("重复规则目标参数");
        }
    }
}
