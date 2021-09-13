package com.medusa.gruul.order.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.medusa.gruul.common.data.base.BaseEntity;
import com.medusa.gruul.order.api.enums.DeliverTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * <p>
 * 订单收货信息表
 * </p>
 *
 * @author alan
 * @since 2019-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_order_delivery")
@ApiModel(value = "OrderDelivery对象", description = "订单收货信息表")
public class OrderDelivery extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 订单物流id
     */
    @ApiModelProperty(value = "订单物流id")
    @TableId(value = "order_id", type = IdType.INPUT)
    private Long orderId;


    /**
     * 商铺ID
     */
    @ApiModelProperty(value = "商铺ID")
    @TableField("shop_id")
    private String shopId;


    /**
     * 配送方式
     */
    @ApiModelProperty(value = "配送方式")
    @TableField("delivery_type")
    private DeliverTypeEnum deliveryType;

    /**
     * 发货时间
     */
    @ApiModelProperty(value = "发货时间")
    @TableField("delivery_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime deliveryTime;

    /**
     * 物流公司
     */
    @ApiModelProperty(value = "物流公司")
    @TableField("delivery_company")
    private String deliveryCompany;

    /**
     * 物流单号
     */
    @ApiModelProperty(value = "物流单号")
    @TableField("delivery_sn")
    private String deliverySn;

    /**
     * 收货人姓名
     */
    @ApiModelProperty(value = "收货人姓名")
    @TableField("receiver_name")
    private String receiverName;

    /**
     * 收货人电话
     */
    @ApiModelProperty(value = "收货人电话")
    @TableField("receiver_phone")
    private String receiverPhone;

    /**
     * 收货人邮编
     */
    @ApiModelProperty(value = "收货人邮编")
    @TableField("receiver_post_code")
    private String receiverPostCode;

    /**
     * 省份/直辖市
     */
    @ApiModelProperty(value = "省份/直辖市")
    @TableField("receiver_province")
    private String receiverProvince;

    /**
     * 城市
     */
    @ApiModelProperty(value = "城市")
    @TableField("receiver_city")
    private String receiverCity;

    /**
     * 区
     */
    @ApiModelProperty(value = "区")
    @TableField("receiver_region")
    private String receiverRegion;

    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址")
    @TableField("receiver_detail_address")
    private String receiverDetailAddress;

    /**
     * 确认收货状态：0->未确认；1->已确认
     */
    @ApiModelProperty(value = "确认收货状态：0->未确认；1->已确认")
    @TableField("is_received")
    private Boolean received;

    /**
     * 确认收货时间
     */
    @ApiModelProperty(value = "确认收货时间")
    @TableField("receive_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime receiveTime;


    /**
     * 物流公司编码
     */
    @ApiModelProperty(value = "物流公司编码")
    @TableField("delivery_code")
    private String deliveryCode;


    @ApiModelProperty("集包地信息")
    @TableField("package_name")
    private String packageName;

    @ApiModelProperty("集包地信息")
    @TableField("package_code")
    private String packageCode;

    @ApiModelProperty("分拣码")
    @TableField("sorting_code")
    private String sortingCode;


    /**
     * 发货消息订阅
     */
    @ApiModelProperty(value = "发货消息订阅")
    @TableField("delivery_template_id")
    private String deliveryTemplateId;

}
