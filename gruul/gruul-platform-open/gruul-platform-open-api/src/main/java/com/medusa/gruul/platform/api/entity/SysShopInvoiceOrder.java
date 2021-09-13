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

import java.time.LocalDateTime;

/**
 * <p>
 * 发票工单表
 * </p>
 *
 * @author whh
 * @since 2020-08-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_sys_shop_invoice_order")
@ApiModel(value = "SysShopInvoiceOrder对象", description = "发票工单表")
public class SysShopInvoiceOrder extends BaseNoTenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 发票类型：1增值税专用发票、2普通发票 3专业发票
     */
    @ApiModelProperty(value = "发票类型：1增值税专用发票、2普通发票 3专业发票")
    @TableField("type")
    private Integer type;

    /**
     * 抬头类型：1个人或事业单位，2企业
     */
    @ApiModelProperty(value = "抬头类型：1个人或事业单位，2企业")
    @TableField("invoice_rise_type")
    private Integer invoiceRiseType;

    /**
     * 抬头名称
     */
    @ApiModelProperty(value = "抬头名称")
    @TableField("invoice_rise_name")
    private String invoiceRiseName;

    /**
     * 纳税人识别号
     */
    @ApiModelProperty(value = "纳税人识别号")
    @TableField("invoice_taxpayer_num")
    private String invoiceTaxpayerNum;

    /**
     * 发票金额
     */
    @ApiModelProperty(value = "发票金额")
    @TableField("amount")
    private String amount;

    /**
     * 邮件地址
     */
    @ApiModelProperty(value = "邮件地址")
    @TableField("email")
    private String email;

    /**
     * 审核状态：0待审核，1已审核
     */
    @ApiModelProperty(value = "审核状态：0待审核，1已审核")
    @TableField("status")
    private Integer status;

    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    @TableField("order_id")
    private Long orderId;
    /**
     * 订单类型 1-充值订单  2-套餐订购订单
     */
    @ApiModelProperty(value = "订单类型 1-充值订单  2-套餐订购订单")
    @TableField("order_type")
    private Integer orderType;

    /**
     * 商户id
     */
    @ApiModelProperty(value = "商户id")
    @TableField("account_id")
    private Long accountId;

    @ApiModelProperty(value = "审核时间")
    @TableField("audit_time")
    private LocalDateTime auditTime;

    @ApiModelProperty(value = "发票编号")
    @TableField("number_no")
    private String numberNo;
}
