package com.medusa.gruul.order.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * The type Receiver address dto.
 * <p>
 * 修改收货地址
 *
 * @author alan
 * @date 2019 /11/10 9:21
 */
@Data
@ApiModel(value = "修改收货地址")
public class ReceiverAddressDto implements Serializable {
    private static final long serialVersionUID = 1L;


    @NotNull
    @ApiModelProperty(value = "要修改的订单的ID")
    private Long orderId;

    /**
     * 省份/直辖市
     */
    @NotBlank
    @ApiModelProperty(value = "省份/直辖市")
    private String receiverProvince;

    /**
     * 城市
     */
    @NotBlank
    @ApiModelProperty(value = "城市")
    private String receiverCity;

    /**
     * 区
     */
    @NotBlank
    @ApiModelProperty(value = "区")
    private String receiverRegion;

    /**
     * 详细地址
     */
    @NotBlank
    @ApiModelProperty(value = "详细地址")
    private String receiverDetailAddress;

    /**
     * 收货人姓名
     */
    @NotBlank
    @ApiModelProperty(value = "收货人姓名")
    private String receiverName;

    /**
     * 收货人电话
     */
    @NotBlank
    @ApiModelProperty(value = "收货人电话")
    private String receiverPhone;

    /**
     * 收货人邮编
     */
    @ApiModelProperty(value = "收货人邮编")
    private String receiverPostCode;
}
