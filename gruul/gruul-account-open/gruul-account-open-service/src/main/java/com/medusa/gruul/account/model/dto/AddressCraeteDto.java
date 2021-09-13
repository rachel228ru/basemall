package com.medusa.gruul.account.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author whh
 * @description
 * @data: 2019/11/19
 */
@Data
public class AddressCraeteDto {

    @ApiModelProperty(value = "省")
    @NotBlank(message = "省为空")
    private String province;

    @ApiModelProperty(value = "市")
    @NotBlank(message = "市为空")
    private String city;

    @ApiModelProperty(value = "区")
    @NotBlank(message = "区为空")
    private String county;

    @ApiModelProperty(value = "收货人电话")
    @NotBlank(message = "收货人电话为空")
    private String phone;

    @ApiModelProperty(value = "详细地址")
    @NotBlank(message = "详细地址为空")
    private String detailInfo;

    @ApiModelProperty(value = "提货人姓名")
    @NotBlank(message = "提货人姓名为空")
    private String userName;


    @ApiModelProperty(value = "经度,维度")
    @NotBlank(message = "经度,维度为空")
    private String location;

    @ApiModelProperty(value = "编码")
    private String postCode;
}
