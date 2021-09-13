package com.medusa.gruul.platform.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.medusa.gruul.common.data.base.BaseNoTenantEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 账号余额明细表
 * </p>
 *
 * @author whh
 * @since 2020-08-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_platform_account_balance_record")
@ApiModel(value = "PlatformAccountBalanceRecord对象", description = "账号余额明细表")
public class PlatformAccountBalanceRecord extends BaseNoTenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商户id
     */
    @ApiModelProperty(value = "商户id")
    @TableField("account_id")
    private Long accountId;

    /**
     * 操作类型 1:充值 2:套餐购买 3:套餐续费
     */
    @ApiModelProperty(value = "操作类型 1:充值 2:套餐购买 3:套餐续费")
    @TableField("consumption_type")
    private Integer consumptionType;

    /**
     * 消费订单号(跟着操作类型走)
     */
    @ApiModelProperty(value = "消费订单号(跟着操作类型走)")
    @TableField("order_num")
    private String orderNum;

    /**
     * 变更之前金额
     */
    @ApiModelProperty(value = "变更之前金额")
    @TableField("before_amount")
    private BigDecimal beforeAmount;

    /**
     * 变更之后金额
     */
    @ApiModelProperty(value = "变更之后金额")
    @TableField("after_amount")
    private BigDecimal afterAmount;

    /**
     * 变更金额
     */
    @ApiModelProperty(value = "变更金额")
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 变更类型：1:收入2:支出
     */
    @ApiModelProperty(value = "变更类型：1:收入2:支出")
    @TableField("type")
    private Integer type;


}
