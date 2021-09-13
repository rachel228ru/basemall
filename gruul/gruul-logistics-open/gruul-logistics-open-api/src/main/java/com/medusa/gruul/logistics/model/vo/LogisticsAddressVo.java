package com.medusa.gruul.logistics.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhaozheng
 */
@Data
@NoArgsConstructor
@ApiModel("查询收发货地址")
public class LogisticsAddressVo {

    @ApiModelProperty("主键 id,新增不传,修改传原值")
    private Long id;

    @ApiModelProperty("联系人名称")
    private String name;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "省id")
    private String provinceId;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "市id")
    private String cityId;

    @ApiModelProperty(value = "县")
    private String country;

    @ApiModelProperty(value = "县id")
    private String countryId;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("联系电话")
    private String phone;

    @ApiModelProperty("邮编")
    private String zipCode;

    @ApiModelProperty(value = "是否为默认发货地址: 0=不是,1=是")
    private Integer defSend;

    @ApiModelProperty(value = "是否为默认退货地址: 0=不是,1=是")
    private Integer defReceive;

    @ApiModelProperty("纬度")
    private String lat;

    @ApiModelProperty("经度")
    private String lng;

}
