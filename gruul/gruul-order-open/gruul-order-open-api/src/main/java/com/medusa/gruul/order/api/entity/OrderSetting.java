package com.medusa.gruul.order.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.medusa.gruul.common.data.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <p>
 * 订单设置表
 * </p>
 *
 * @author alan
 * @since 2019-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_order_setting")
@ApiModel(value = "OrderSetting对象", description = "订单设置表")
public class OrderSetting extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 秒杀订单超时关闭时间(分)
     */
    @ApiModelProperty(value = "秒杀订单超时关闭时间(分)")
    @TableField("flash_order_overtime")
    private Long flashOrderOvertime;

    /**
     * 正常订单超时时间(分)
     */
    @ApiModelProperty(value = "正常订单超时时间(分)")
    @TableField("normal_order_overtime")
    private Long normalOrderOvertime;

    /**
     * 发货后自动确认收货时间（天）
     */
    @Min(0)
    @Max(30)
    @ApiModelProperty(value = "发货后自动确认收货时间（天）")
    @TableField("confirm_overtime")
    private Integer confirmOvertime;

    /**
     * 自动完成交易时间，不能申请售后（天）
     */
    @Min(0)
    @Max(30)
    @ApiModelProperty(value = "自动完成交易时间，不能申请售后（天）")
    @TableField("finish_overtime")
    private Integer finishOvertime;

    /**
     * 未启用
     * 订单完成后自动好评时间（天）
     */
    @Min(0)
    @Max(30)
    @ApiModelProperty(value = "订单完成后自动好评时间（天）")
    @TableField("comment_overtime")
    private Integer commentOvertime;

    /**
     * 是否开启订单评论
     */
    @ApiModelProperty(value = "是否开启订单评论")
    @TableField("open_evaluate")
    private Boolean openEvaluate;


    /**
     * 最大售后申请次数
     */
    @ApiModelProperty(value = "最大售后申请次数")
    @TableField("afs_apply_number")
    private Integer afsApplyNumber;


    /**
     * 商家最大审核期限(天)
     */
    @Min(0)
    @Max(30)
    @ApiModelProperty(value = "商家最大审核期限(天)")
    @TableField("merchant_confirm_overtime")
    private Integer merchantConfirmOvertime;


    /**
     * 用户最大退货期限(天)
     */
    @Min(0)
    @Max(30)
    @ApiModelProperty(value = "用户最大退货期限(天)")
    @TableField("user_return_overtime")
    private Integer userReturnOvertime;


    /**
     * 快递AppId
     */
    @ApiModelProperty(value = "快递AppId")
    @TableField("kd_app_id")
    private String kdAppId;

    /**
     * 快递AppKey
     */
    @ApiModelProperty(value = "快递AppKey")
    @TableField("kd_app_key")
    private String kdAppKey;

    /**
     * 支付方式
     */
    @NotBlank
    @ApiModelProperty(value = "支付方式：1->微信支付;2->余额支付;3->好友代付;")
    @TableField("payment_model")
    private String paymentModel;

    /**
     * 自定义表单
     */
    @ApiModelProperty(value = "自定义表单")
    @TableField("custom_from")
    private String customFrom;


    /**
     * 是否开启下单通知：0->关闭；1->开启
     */
    @NotNull
    @ApiModelProperty(value = "是否开启下单通知：0->关闭；1->开启")
    @TableField("order_notify")
    private Boolean orderNotify;


}
