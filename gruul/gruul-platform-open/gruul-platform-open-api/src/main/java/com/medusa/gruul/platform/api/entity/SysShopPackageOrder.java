package com.medusa.gruul.platform.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.medusa.gruul.common.data.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 店铺套餐订单表
 * </p>
 *
 * @author whh
 * @since 2020-08-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_sys_shop_package_order")
@ApiModel(value = "SysShopPackageOrder对象", description = "店铺套餐订单表")
public class SysShopPackageOrder extends BaseEntity {

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
     * 店铺模版表id
     */
    @ApiModelProperty(value = "店铺模版表id")
    @TableField("shop_template_info_id")
    private Long shopTemplateInfoId;

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    @TableField("order_num")
    private String orderNum;

    /**
     * 套餐 id
     */
    @ApiModelProperty(value = "套餐 id")
    @TableField("package_id")
    private Long packageId;

    /**
     * 下单时套餐完整数据 com.medusa.gruul.platform.api.entity.SysShopPackage
     */
    @ApiModelProperty(value = "下单时套餐完整数据")
    @TableField("package_data")
    private String packageData;

    /**
     * 套餐名称
     */
    @ApiModelProperty(value = "套餐名称")
    @TableField("package_name")
    private String packageName;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    @TableField("shop_name")
    private String shopName;


    /**
     * 模版id
     */
    @ApiModelProperty(value = "店铺模板名称(店铺类型)")
    @TableField("template_name")
    private String templateName;


    @ApiModelProperty(value = "订单类型 1-订购  2-续费  3-升级")
    @TableField("order_type")
    private Integer orderType;

    /**
     * 套餐时长
     */
    @ApiModelProperty(value = "套餐时长")
    @TableField("package_time")
    private Integer packageTime;

    /**
     * 套餐价格单位 1天，2月，3年
     */
    @ApiModelProperty(value = "套餐价格单位 1天，2月，3年")
    @TableField("package_price_unit")
    private Integer packagePriceUnit;

    /**
     * 套餐开始时间
     */
    @ApiModelProperty(value = "套餐开始时间")
    @TableField("package_start_time")
    private LocalDateTime packageStartTime;

    /**
     * 套餐结束时间
     */
    @ApiModelProperty(value = "套餐结束时间")
    @TableField("package_end_time")
    private LocalDateTime packageEndTime;

    /**
     * 套餐价格
     */
    @ApiModelProperty(value = "套餐价格")
    @TableField("package_price")
    private BigDecimal packagePrice;

    /**
     * 应付金额
     */
    @ApiModelProperty(value = "应付金额")
    @TableField("amount_payable")
    private BigDecimal amountPayable;

    /**
     * 实付金额
     */
    @ApiModelProperty(value = "实付金额")
    @TableField("paid_payable")
    private BigDecimal paidPayable;

    /**
     * 支付方式  1:余额支付2:微信3:支付宝4:汇款支付 5:系统支付
     */
    @ApiModelProperty(value = "支付方式  1:余额支付2:微信3:支付宝 4:汇款支付 5:系统支付 ")
    @TableField("pay_type")
    private Integer payType;

    /**
     * 订单支付状态 0:待处理1:处理中2:已经完成 3:关闭
     */
    @ApiModelProperty(value = "订单支付状态  0:待处理1:处理中2:已经完成 3:关闭")
    @TableField("status")
    private Integer status;

    /**
     * 汇款支付时填写支付方信息json
     */
    @ApiModelProperty(value = "汇款支付时填写支付方信息json")
    @TableField("pay_info")
    private String payInfo;

    /**
     * 审核人id
     */
    @ApiModelProperty(value = "审核人id")
    @TableField("relauditor_id")
    private Long relauditorId;

    /**
     * 审核人名称
     */
    @ApiModelProperty(value = "审核人名称")
    @TableField("auditor_name")
    private String auditorName;

    /**
     * 审核状态：0:待审核 1:审核通过 2:审核拒绝
     */
    @ApiModelProperty(value = "审核状态：0:待审核 1:审核通过 2:审核拒绝")
    @TableField("auditor_status")
    private Integer auditorStatus;

    /**
     * 是否同意协议  1-同意
     */
    @ApiModelProperty(value = "是否同意协议  1-同意")
    @TableField("is_agreed")
    private Integer isAgreed;

    /**
     * 是否同意标准版自动扣除续费
     */
    @ApiModelProperty(value = "是否同意标准版自动扣除续费 是否自动续费 0-不自动  1-自动")
    @TableField("is_automatic_deduction")
    private Integer isAutomaticDeduction;

    /**
     * 是否收到汇款 0:未收到 1:已收到
     */
    @ApiModelProperty(value = "是否收到汇款 0:未收到 1:已收到")
    @TableField("is_received")
    private Integer isReceived;

    /**
     * 开票状态
     */
    @ApiModelProperty(value = "开票状态  0-未开票 1-开票中 2-已开票")
    @TableField("invoice_status")
    private Integer invoiceStatus;


    /**
     * 订单来源
     */
    @ApiModelProperty(value = "订单来源   0-用户购买 " +
            "1-管理台购买（店铺列表，购买，续费） 2-平台赠送（用户创建店铺时自动赠送）" +
            " 3-平台创建（为指定商户直接创建指定套餐店铺）4-代理付费")
    @TableField("order_source")
    private Integer orderSource;


    @ApiModelProperty(value = "审核时间")
    @TableField("audit_time")
    private LocalDateTime auditTime;

    @ApiModelProperty(value = "代理付费id")
    @TableField("agent_id")
    private Long agentId;
}
