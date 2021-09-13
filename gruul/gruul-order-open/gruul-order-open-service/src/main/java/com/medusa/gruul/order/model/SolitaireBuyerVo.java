package com.medusa.gruul.order.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The type Solitaire buyer vo.
 * <p>
 * 接龙购买者的信息
 *
 * @author alan
 * @date 2019 /10/6 12:38
 */
@Data
@ApiModel(value = "接龙购买者的信息")
public class SolitaireBuyerVo {

    /**
     * 序号
     */
    @ApiModelProperty(value = "序号")
    private Integer sn;

    /**
     * 用户帐号
     */
    @ApiModelProperty(value = "用户帐号")
    private String userName;

    /**
     * 订单ID
     */
    @ApiModelProperty(value = "订单ID")
    private Long orderId;

    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    private String userAvatarUrl;

    /**
     * 支付时间
     */
    @ApiModelProperty(value = "支付时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payTime;

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
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址")
    private String receiverDetailAddress;


    /**
     * 订单支付金额
     */
    @ApiModelProperty(value = "订单支付金额")
    private BigDecimal payAmount;

    /**
     * 订单商品详情
     */
    @ApiModelProperty(value = "订单商品详情")
    private List<SolitaireOrderItemVo> itemVoList;

}
