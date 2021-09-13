package com.medusa.gruul.order.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.medusa.gruul.common.data.base.BaseEntity;
import com.medusa.gruul.order.api.enums.OrderStatusEnum;
import com.medusa.gruul.order.api.enums.OrderTypeEnum;
import com.medusa.gruul.order.api.enums.PayTypeEnum;
import com.medusa.gruul.order.api.enums.SourceTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author alan
 * @since 2019-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_order")
@ApiModel(value = "Order对象", description = "订单表")
public class Order extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 商铺ID
     */
    @ApiModelProperty(value = "商铺ID")
    @TableField("shop_id")
    private String shopId;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private String userId;

    /**
     * 用户帐号
     */
    @ApiModelProperty(value = "用户帐号")
    @TableField("user_name")
    private String userName;

    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    @TableField("user_avatar_url")
    private String userAvatarUrl;

    /**
     * 用户备注
     */
    @ApiModelProperty(value = "用户备注")
    @TableField("user_note")
    private String userNote;

    /**
     * 订单类型
     */
    @ApiModelProperty(value = "订单类型")
    @TableField("type")
    private OrderTypeEnum type;

    /**
     * 订单总金额=商品指导价*商品数量
     */
    @ApiModelProperty(value = "订单总金额")
    @TableField("total_amount")
    private BigDecimal totalAmount;

    /**
     * 实际金额=应付金额-退款金额
     */
    @ApiModelProperty(value = "实际金额")
    @TableField("discounts_amount")
    private BigDecimal discountsAmount;

    /**
     * 应付金额（实际支付金额）=订单总金额+运费-促销优化金额
     */
    @ApiModelProperty(value = "应付金额（实际支付金额）")
    @TableField("pay_amount")
    private BigDecimal payAmount;

    /**
     * 运费金额=快递运费或送货上门的费用
     */
    @ApiModelProperty(value = "运费金额")
    @TableField("freight_amount")
    private BigDecimal freightAmount;

    /**
     * 促销优化金额=积分抵扣+优惠券+满减+会员价+会员免运费
     */
    @ApiModelProperty(value = "促销优化金额（促销价、满减、会员价）")
    @TableField("promotion_amount")
    private BigDecimal promotionAmount;


    /**
     * 优惠券id
     */
    @ApiModelProperty(value = "优惠券id")
    @TableField("coupon_id")
    private Long couponId;

    /**
     * 优惠券抵扣金额
     */
    @ApiModelProperty(value = "优惠券抵扣金额")
    @TableField("coupon_amount")
    private BigDecimal couponAmount;


    /**
     * 满减活动ID
     */
    @ApiModelProperty(value = "满减活动ID")
    @TableField("full_scale_id")
    private Long fullScaleId;

    /**
     * 满减优惠金额
     */
    @ApiModelProperty(value = "满减优惠金额")
    @TableField("full_scale_amount")
    private BigDecimal fullScaleAmount;


    /**
     * 支付方式
     */
    @ApiModelProperty(value = "支付方式")
    @TableField("pay_type")
    private PayTypeEnum payType;

    /**
     * 支付流水号
     */
    @ApiModelProperty(value = "支付流水号")
    @TableField("transaction_id")
    private String transactionId;

    /**
     * 支付时间
     */
    @ApiModelProperty(value = "支付时间")
    @TableField("pay_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime payTime;

    /**
     * 订单来源
     */
    @ApiModelProperty(value = "订单来源")
    @TableField("source_type")
    private SourceTypeEnum sourceType;

    /**
     * 订单状态
     */
    @ApiModelProperty(value = "订单状态")
    @TableField("status")
    private OrderStatusEnum status;

    /**
     * 关闭时间
     */
    @ApiModelProperty(value = "关闭时间")
    @TableField("close_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime closeTime;


    /**
     * 订单备注
     */
    @ApiModelProperty(value = "订单备注")
    @TableField("note")
    private String note;

    /**
     * 评价时间
     */
    @ApiModelProperty(value = "评价时间")
    @TableField("comment_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime commentTime;

    /**
     * 完成时间
     */
    @ApiModelProperty(value = "完成时间")
    @TableField("complete_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime completeTime;

    /**
     * 预计到货时间
     */
    @ApiModelProperty(value = "预计到货时间")
    @TableField("estimated_delivery_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime estimatedDeliveryTime;

    /**
     * 过期时间
     */
    @ApiModelProperty(value = "过期时间")
    @TableField("expire_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime expireTime;


    /**
     * 自定义字段:[{"key":"IDCard","value":"332022199001010011"},{"key":"Phone","value":"13956852259"}]
     */
    @ApiModelProperty(value = "自定义字段:[{\"key\":\"IDCard\",\"value\":\"332022199001010011\"},{\"key\":\"Phone\"," +
            "\"value\":\"13956852259\"}]")
    @TableField("custom_form")
    private String customForm;


    /**
     * 退款金额
     */
    @ApiModelProperty(value = "退款金额")
    @TableField("refund_amount")
    private BigDecimal refundAmount;

    /**
     * 退款支付流水号
     */
    @ApiModelProperty(value = "退款支付流水号")
    @TableField("refund_transaction_id")
    private String refundTransactionId;


}
