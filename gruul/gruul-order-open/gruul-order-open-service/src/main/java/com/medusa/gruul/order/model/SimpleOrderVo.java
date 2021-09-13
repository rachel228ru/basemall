package com.medusa.gruul.order.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.medusa.gruul.afs.api.entity.AfsOrder;
import com.medusa.gruul.order.api.enums.OrderTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * The type Simple order vo.
 * <p>
 * 简单的订单结果
 *
 * @author alan
 * @date 2019 /11/13 20:20
 */
@Data
@ApiModel(value = "简单的订单结果")
public class SimpleOrderVo implements Serializable {

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
     * 发货单名称
     */
    @ApiModelProperty(value = "发货单名称")
    private String sendBillName;

    /**
     * 用户备注
     */
    @ApiModelProperty(value = "用户备注")
    private String userNote;

    /**
     * 自定义表单
     */
    @ApiModelProperty(value = "自定义表单")
    private String customForm;

    /**
     * 订单的售后详情
     */
    @ApiModelProperty(value = "订单的售后详情")
    private List<AfsOrder> afsOrderList;

    /**
     * 订单单个商品详情
     */
    @ApiModelProperty(value = "订单单个商品详情")
    private List<SimpleOrderItemVo> itemVoList;

}
