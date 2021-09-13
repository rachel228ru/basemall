package com.medusa.gruul.order.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.medusa.gruul.afs.api.entity.AfsOrder;
import com.medusa.gruul.order.api.enums.DeliverTypeEnum;
import com.medusa.gruul.order.api.enums.OrderStatusEnum;
import com.medusa.gruul.order.api.enums.OrderTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * The type Manage order vo.
 * <p>
 * 管理端订单返回结果
 *
 * @author alan
 * @date 2019 /11/5 21:29
 */
@Data
@ApiModel(value = "管理端订单返回结果")
public class ManageOrderVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id", example = "1")
    private Long orderId;

    /**
     * 订单类型
     */
    @ApiModelProperty(value = "订单类型")
    private OrderTypeEnum type;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private String createTime;


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
     * 收货人邮编
     */
    @ApiModelProperty(value = "收货人邮编")
    private String receiverPostCode;

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
     * 物流公司
     */
    @ApiModelProperty(value = "物流公司")
    private String deliveryCompany;


    /**
     * 物流单号
     */
    @ApiModelProperty(value = "物流单号")
    private String deliverySn;


    /**
     * 订单支付金额
     */
    @ApiModelProperty(value = "订单支付金额")
    private BigDecimal payAmount;


    /**
     * 订单状态
     */
    @ApiModelProperty(value = "订单状态")
    private OrderStatusEnum status;


    /**
     * 商家备注
     */
    @ApiModelProperty(value = "商家备注")
    private String note;


    /**
     * 订单单个商品详情
     */
    @ApiModelProperty(value = "订单单个商品详情")
    private List<SimpleOrderItemVo> itemVoList;


    /**
     * 订单的售后详情
     */
    @ApiModelProperty(value = "订单的售后详情")
    private List<AfsOrder> afsOrderList;
}
