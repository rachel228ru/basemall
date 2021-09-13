package com.medusa.gruul.logistics.model.dto.manager;

import cn.hutool.core.bean.BeanUtil;
import com.medusa.gruul.logistics.api.entity.LogisticsExpress;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 物流公司发货地址设置
 *
 * @author lcysike
 */
@Data
@ApiModel("新增或修改物流公司发货地址设置")
public class LogisticsExpressDto {

    @ApiModelProperty("主键 id,新增不传,修改传原值")
    private Long id;


    @NotNull
    @ApiModelProperty(value = "快递公司名称")
    private String name;

    @NotNull
    @ApiModelProperty(value = "快递公司code")
    private String code;

    @NotNull
    @ApiModelProperty(value = "快递公司客服电话")
    private String phone;

    @NotNull
    @ApiModelProperty(value = "地址id 关联物流发货地址表id")
    private Long addressId;

    @NotNull
    @ApiModelProperty(value = "客户id")
    private String customerName;

    @NotNull
    @ApiModelProperty(value = "客户密匙")
    private String customerPassword;

    @NotNull
    @ApiModelProperty(value = "负责人")
    private String linkName;

    @NotNull
    @ApiModelProperty(value = "负责人电话")
    private String linkTel;

    @ApiModelProperty(value = "状态")
    private String status;

    public LogisticsExpress coverBean() {
        LogisticsExpress logisticsExpress = new LogisticsExpress();
        BeanUtil.copyProperties(this, logisticsExpress);
        return logisticsExpress;
    }
}