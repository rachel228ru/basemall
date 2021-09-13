package com.medusa.gruul.platform.model.dto.agent;

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
 * @data: 2020/10/22
 */
@Data
public class AgentInfoListDto {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "账户")
    private String account;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "联系人")
    private String linkName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "代理到期时间")
    private LocalDateTime nextDueTime;

    @ApiModelProperty(value = "邀请码")
    private String code;

    @ApiModelProperty(value = "代理状态1:正常 2:冻结 3:停用")
    private Integer status;

    @ApiModelProperty(value = "累计销售总金额")
    private BigDecimal totalSalePrice;

    @ApiModelProperty(value = "累计销售总单数")
    private Integer totalSaleCount;

    @ApiModelProperty(value = "可用金额")
    private BigDecimal availableAmount;

    @ApiModelProperty(value = "佣金金额")
    private BigDecimal commissionAmount;

    @ApiModelProperty(value = "下集渠道商数")
    private Integer channalCount;

    @ApiModelProperty(value = "商户数量")
    private Integer shopInfoCount;

    @ApiModelProperty(value = "店铺数量")
    private Integer appInfoCount;

    @ApiModelProperty(value = "父节点id，间接渠道商时有值，代理商和直属渠道商时为0")
    private Long parentId;
}
