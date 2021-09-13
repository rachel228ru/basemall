package com.medusa.gruul.platform.model.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author whh
 * @description
 * @data: 2020/8/15
 */
@Data
public class PackageOrderVo {

    @ApiModelProperty(value = "id,订单id")
    private Long id;

    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户名称")
    private String nikeName;

    /**
     * 头像url
     */
    @ApiModelProperty(value = "头像url")
    private String avatarUrl;

    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码")
    private String phone;


    /**
     * 套餐名称
     */
    @ApiModelProperty(value = "套餐名称")
    private String packageName;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String shopName;


    /**
     * 模版id
     */
    @ApiModelProperty(value = "店铺模板名称(店铺类型)")
    private String templateName;


    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String orderNum;

    /**
     * 下单时套餐完整数据  com.medusa.gruul.platform.api.entity.SysShopPackage
     */
    @ApiModelProperty(value = "下单时套餐完整数据")
    private String packageData;


    @ApiModelProperty(value = "订单类型 1-订购  2-续费  3-升级")
    private Integer orderType;

    /**
     * 套餐时长
     */
    @ApiModelProperty(value = "套餐时长")
    private Integer packageTime;

    /**
     * 套餐价格单位 1天，2月，3年
     */
    @ApiModelProperty(value = "套餐价格单位 1天，2月，3年")
    private Integer packagePriceUnit;


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
     * 1:余额支付2:微信3:支付宝4:汇款支付
     */
    @ApiModelProperty(value = " 支付方式  1:余额支付2:微信3:支付宝4:汇款支付 5:系统支付 ")
    @TableField("pay_type")
    private Integer payType;

    /**
     * 订单支付状态 0:待处理1:处理中2:已经完成  3:已关闭
     */
    @ApiModelProperty(value = "订单支付状态 0:待处理1:处理中2:已经完成 3:已关闭")
    private Integer status;

    /**
     * 汇款支付时填写支付方信息json
     */
    @ApiModelProperty(value = "汇款支付时填写支付方信息json")
    private String payInfo;


    /**
     * 审核状态：0:待审核 1:审核通过 2:审核拒绝
     */
    @ApiModelProperty(value = "审核状态：0:待审核 1:审核通过 2:审核拒绝")
    private Integer auditorStatus;


    @ApiModelProperty(value = "开票状态  0-未开票 1-开票中 2-已开票")
    private Integer invoiceStatus;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "审核时间")
    private LocalDateTime auditTime;


    /**
     * 订单来源
     */
    @ApiModelProperty(value = "订单来源   0-用户购买 1-管理台购买（店铺列表，购买，续费） 2-平台赠送（用户创建店铺时自动赠送） 3-平台创建（为指定商户直接创建指定套餐店铺）4-代理付费")
    private Integer orderSource;


}
