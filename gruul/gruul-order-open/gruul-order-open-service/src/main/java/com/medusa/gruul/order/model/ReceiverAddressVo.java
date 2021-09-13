package com.medusa.gruul.order.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * The type Receiver address vo.
 * <p>
 * 用户可用收货地址
 *
 * @author alan
 * @date 2019 /10/6 12:38
 */
@Data
@ApiModel(value = "用户可用收货地址")
public class ReceiverAddressVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 收货人姓名
     */
    @ApiModelProperty(value = "收货人姓名")
    private String userName;

    /**
     * 收货人联系电话
     */
    @ApiModelProperty(value = "收货人联系电话")
    private String phone;

    /**
     * 邮编
     */
    @ApiModelProperty(value = "邮编")
    private String postCode;

    /**
     * 是否默认 0-非默认 1-默认
     */
    @ApiModelProperty(value = "是否默认 0-非默认 1-默认")
    private Boolean isDefault;

    /**
     * 省
     */
    @ApiModelProperty(value = "省")
    private String province;

    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    private String city;

    /**
     * 区
     */
    @ApiModelProperty(value = "区")
    private String county;

    /**
     * 详细收货地址信息
     */
    @ApiModelProperty(value = "详细收货地址信息")
    private String detailInfo;

    /**
     * 收货地址关联线路id
     */
    @ApiModelProperty(value = "收货地址关联线路id", example = "1")
    private Long lineId;

}
