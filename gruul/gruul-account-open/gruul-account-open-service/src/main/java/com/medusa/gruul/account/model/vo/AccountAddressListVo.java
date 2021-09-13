package com.medusa.gruul.account.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 * @description
 * @data: 2019/11/19
 */
@Data
public class AccountAddressListVo {

    @ApiModelProperty(value = "地址id")
    private Long id;

    @ApiModelProperty(value = "收货人姓名")
    private String userName;

    @ApiModelProperty(value = "收货人联系电话")
    private String phone;

    @ApiModelProperty(value = "是否默认 0-非默认 1-默认")
    private Integer isDefault;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区")
    private String county;

    @ApiModelProperty(value = "详细收货地址信息")
    private String detailInfo;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "经度,维度")
    private String location;

    @ApiModelProperty(value = "编码")
    private String postCode;

}
