package com.medusa.gruul.logistics.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * 物流公司发货地址设置
 *
 * @author lcysike
 */
@Data
@ApiModel("查询物流公司发货地址设置")
public class LogisticsExpressVo {

    @ApiModelProperty("主键 id")
    private Long id;

    @ApiModelProperty(value = "快递公司名称")
    private String name;

    @ApiModelProperty(value = "快递公司code")
    private String code;

    @ApiModelProperty(value = "快递公司客服电话")
    private String phone;

    @ApiModelProperty(value = "地址id 关联物流发货地址表id")
    private Long addressId;

    @ApiModelProperty(value = "客户id")
    private String customerName;

    @ApiModelProperty(value = "客户密匙")
    private String customerPassword;

    @ApiModelProperty(value = "负责人")
    private String linkName;

    @ApiModelProperty(value = "负责人电话")
    private String linkTel;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "物流公司关联地址信息")
    private List<LogisticsAddressVo> logisticsAddressVos;
}