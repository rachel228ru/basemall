package com.medusa.gruul.logistics.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 物流公司与发货信息查询 物流公司设置表与退发货地址管理表联合查询对象
 *
 * @author lcysike
 */
@Data
@ApiModel("物流公司与发货信息查询")
public class LogisticsExpressAddressVo {

    @ApiModelProperty(value = "expressId")
    private Long expressId;

    @ApiModelProperty(value = "快递公司名称")
    private String expressName;

    @ApiModelProperty(value = "快递公司code")
    private String expressCode;

    @ApiModelProperty(value = "发货地址id 关联物流发货地址表id")
    private Long addressId;

    @ApiModelProperty(value = "客户id")
    private String customerName;

    @ApiModelProperty(value = "客户密匙")
    private String customerPassword;

    @ApiModelProperty(value = "发货人所在省")
    private String province;

    @ApiModelProperty(value = "发货人所在市")
    private String city;

    @ApiModelProperty(value = "发货人所在县")
    private String country;

    @ApiModelProperty("发货人地址")
    private String address;

    @ApiModelProperty("发货人名称")
    private String receiveName;

    @ApiModelProperty("发货人电话")
    private String receivePhone;

    @ApiModelProperty("发货人邮编")
    private String zipCode;

}