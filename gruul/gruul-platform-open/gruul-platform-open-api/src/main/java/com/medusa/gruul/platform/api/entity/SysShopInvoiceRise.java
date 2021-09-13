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

/**
 * <p>
 * 用户发票抬头表
 * </p>
 *
 * @author whh
 * @since 2020-08-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_sys_shop_invoice_rise")
@ApiModel(value = "SysShopInvoiceRise对象", description = "用户发票抬头表")
public class SysShopInvoiceRise extends BaseNoTenantEntity {

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
     * 抬头类型：1个人或事业单位，2企业
     */
    @ApiModelProperty(value = "抬头类型：1个人或事业单位，2企业")
    @TableField("head_type")
    private Integer headType;

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
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @TableField("remark")
    private String remark;

    /**
     * 是否默认使用 0-否  1-是
     */
    @ApiModelProperty(value = "是否默认使用 0-否  1-是")
    @TableField("default_status")
    private Integer defaultStatus;


}
