package com.medusa.gruul.afs.api.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.medusa.gruul.afs.api.enums.AfsOrderCloseTypeEnum;
import com.medusa.gruul.afs.api.enums.AfsOrderStatusEnum;
import com.medusa.gruul.afs.api.enums.AfsOrderTypeEnum;
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
 * 售后工单
 * </p>
 *
 * @author alan
 * @since 2020 -08-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_afs_order")
@ApiModel(value = "AfsOrder对象", description = "售后工单")
public class AfsOrder extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 工单id
     */
    @ApiModelProperty(value = "工单id")
    @TableId("id")
    private Long id;

    /**
     * 商铺ID
     */
    @ApiModelProperty(value = "商铺ID")
    @TableField("shop_id")
    private String shopId;

    /**
     * 工单编号
     */
    @ApiModelProperty(value = "工单编号")
    @TableField("no")
    private String no;

    /**
     * 工单类型
     */
    @ApiModelProperty(value = "工单类型：REFUND->退款;" +
            "RETURN_REFUND->退货退款;")
    @TableField("type")
    private AfsOrderTypeEnum type;

    /**
     * 审核状态
     */
    @ApiModelProperty(value = "审核状态：WAIT_FOR_BUSINESS_APPROVED->待商家审核;" +
            "WAIT_FOR_RETURN->待退货; " +
            "WAIT_FOR_SEND->待发货;" +
            "SHIPPED->配送中;" +
            "SUCCESS->成功;" +
            "CLOSE->已关闭")
    @TableField("status")
    private AfsOrderStatusEnum status;

    /**
     * 关闭原因
     */
    @ApiModelProperty(value = "关闭原因：USER_CANCEL->用户撤销;" +
            "SELLER_REFUSE->卖家拒绝")
    @TableField("close_type")
    private AfsOrderCloseTypeEnum closeType;

    /**
     * 申请人ID
     */
    @ApiModelProperty(value = "申请人ID")
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
     * 商品sku编号
     */
    @ApiModelProperty(value = "商品sku编号")
    @TableField("product_sku_id")
    private Long productSkuId;

    /**
     * 消息订阅ID
     */
    @ApiModelProperty(value = "消息订阅ID	")
    @TableField("template_id")
    private String templateId;

    /**
     * 退货消息订阅ID
     */
    @ApiModelProperty(value = "退货消息订阅ID	")
    private String returnTemplateId;

    /**
     * 售后生成的订单
     */
    @ApiModelProperty(value = "售后生成的订单")
    @TableField("order_id")
    private Long orderId;


    /**
     * 退款金额
     */
    @ApiModelProperty(value = "退款金额")
    @TableField("refund_amount")
    private BigDecimal refundAmount;

    /**
     * 过期时间
     */
    @ApiModelProperty(value = "过期时间")
    @TableField(value = "deadline", updateStrategy = FieldStrategy.IGNORED)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime deadline;

    /**
     * 说明
     */
    @ApiModelProperty(value = "说明")
    @TableField("description")
    private String description;

    /**
     * 照片
     */
    @ApiModelProperty(value = "照片")
    @TableField("images")
    private String images;

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
     * 拒绝时间
     */
    @ApiModelProperty(value = "拒绝时间")
    @TableField("refusal_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime refusalTime;

    /**
     * 成功时间
     */
    @ApiModelProperty(value = "成功时间")
    @TableField("success_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime successTime;

    /**
     * 拒绝原因
     */
    @ApiModelProperty(value = "拒绝原因")
    @TableField("refusal_reason")
    private String refusalReason;

    /**
     * 申请的原因
     */
    @ApiModelProperty(value = "申请的原因")
    @TableField("reason")
    private String reason;

    /**
     * 申请售后的签收单ID
     */
    @ApiModelProperty(value = "申请售后的签收单ID")
    @TableField("receipt_bill_id")
    private Long receiptBillId;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @TableField("note")
    private String note;

    /**
     * 物流单号
     */
    @ApiModelProperty(value = "物流单号")
    @TableField("delivery_sn")
    private String deliverySn;

    /**
     * 物流公司编号
     */
    @ApiModelProperty(value = "物流公司编号")
    @TableField("delivery_code")
    private String deliveryCode;

    /**
     * 物流公司
     */
    @ApiModelProperty(value = "物流公司")
    @TableField("delivery_company")
    private String deliveryCompany;

    /**
     * 是否物流配送
     */
    @ApiModelProperty(value = "是否物流配送")
    @TableField("is_logistics")
    private Boolean isLogistics;


}
