package com.medusa.gruul.logistics.model.dto.manager;

import cn.hutool.core.bean.BeanUtil;
import com.medusa.gruul.logistics.api.entity.LogisticsAddress;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author zhaozheng
 */
@Data
@ApiModel("新增或修改收发货地址")
public class LogisticsAddressDto {

    @ApiModelProperty("主键 id,新增不传,修改传原值")
    private Long id;

    @NotNull
    @ApiModelProperty("联系人名称")
    private String name;

    @NotNull
    @ApiModelProperty(value = "省")
    private String province;

    @NotNull
    @ApiModelProperty(value = "省id")
    private String provinceId;

    @NotNull
    @ApiModelProperty(value = "市")
    private String city;

    @NotNull
    @ApiModelProperty(value = "市id")
    private String cityId;

    @ApiModelProperty(value = "县")
    private String country;

    @ApiModelProperty(value = "县id")
    private String countryId;

    @NotNull
    @ApiModelProperty("地址")
    private String address;

    @NotNull
    @ApiModelProperty("联系电话")
    private String phone;

    @NotNull
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

    public LogisticsAddress coverBean() {
        LogisticsAddress logisticsAddress = new LogisticsAddress();
        BeanUtil.copyProperties(this, logisticsAddress);
        return logisticsAddress;
    }
}
