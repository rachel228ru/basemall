package com.medusa.gruul.afs.model;

import com.medusa.gruul.afs.api.enums.AfsOrderStatusEnum;
import com.medusa.gruul.afs.api.enums.AfsOrderTypeEnum;
import com.medusa.gruul.order.api.enums.DeliverTypeEnum;
import com.medusa.gruul.order.api.enums.OrderStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * The type Manage afs order vo.
 *
 * @author alan
 * @description: 商家售后订单列表
 * @date 2020 /8/5 21:54
 */
@Data
@ApiModel(value = "商家售后订单列表")
public class ManageAfsOrderVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 工单ID
     */
    @ApiModelProperty(value = "工单ID")
    private Long id;

    /**
     * 工单编号
     */
    @ApiModelProperty(value = "工单编号")
    private String no;

    /**
     * 工单类型：3.退款  5.退货退款
     */
    @ApiModelProperty(value = "工单类型：3.退款 5.退货退款")
    private AfsOrderTypeEnum type;

    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片")
    private String productPic;

    /**
     * 商品名
     */
    @ApiModelProperty(value = "商品名")
    private String productName;

    /**
     * 商品规格
     */
    @ApiModelProperty(value = "商品规格")
    private String specs;

    /**
     * 销售价格
     */
    @ApiModelProperty(value = "销售价格")
    private BigDecimal productPrice;

    /**
     * 商品数量
     */
    @ApiModelProperty(value = "商品数量")
    private Integer productQuantity;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", example = "1")
    private Long userId;

    /**
     * 用户帐号
     */
    @ApiModelProperty(value = "用户帐号")
    private String userName;

    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    private String userAvatarUrl;

    /**
     * 配送方式
     */
    @ApiModelProperty(value = "配送方式")
    private DeliverTypeEnum deliveryType;


    /**
     * 收货人姓名
     */
    @ApiModelProperty(value = "收货人姓名")
    private String receiverName;

    /**
     * 收货人电话
     */
    @ApiModelProperty(value = "收货人电话")
    private String receiverPhone;


    /**
     * 省份/直辖市
     */
    @ApiModelProperty(value = "省份/直辖市")
    private String receiverProvince;

    /**
     * 城市
     */
    @ApiModelProperty(value = "城市")
    private String receiverCity;

    /**
     * 区
     */
    @ApiModelProperty(value = "区")
    private String receiverRegion;

    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址")
    private String receiverDetailAddress;

    /**
     * 订单退款金额
     */
    @ApiModelProperty(value = "订单退款金额")
    private BigDecimal refundAmount;

    /**
     * 审核状态：1.待商家审核 2.待退货 4.待发货 5.待提货  6.成功
     */
    @ApiModelProperty(value = "审核状态：1.待商家审核 2.待退货 4.待发货 5.待提货  6.成功")
    private AfsOrderStatusEnum status;

    /**
     * 发货单ID
     */
    @ApiModelProperty(value = "发货单ID")
    private String sendBillId;

    /**
     * 发货单名称
     */
    @ApiModelProperty(value = "发货单名称")
    private String sendBillName;

    /**
     * 申请本次售后的订单ID
     */
    @ApiModelProperty(value = "申请本次售后的订单ID，存在多个的时候为第一个的值")
    private Long orderId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String note;

    /**
     * 订单状态
     */
    @ApiModelProperty(value = "订单状态")
    private OrderStatusEnum orderStatus;
}
