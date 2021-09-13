package com.medusa.gruul.logistics.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 电子面单打印机设置
 *
 * @author lcysike
 */
@Data
@ApiModel(value = "LogisticsExpressPrintVo对象", description = "电子面单打印机设置查询对象")
public class LogisticsExpressPrintVo {

    @ApiModelProperty("主键 id")
    private Long id;

    @ApiModelProperty(value = "设备类型")
    private String deviceType;

    @ApiModelProperty(value = "设备型号")
    private String deviceModel;

    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    @ApiModelProperty(value = "打印机机身号")
    private String deviceNo;

    @ApiModelProperty(value = "打印机KEY")
    private String deviceKey;

    @ApiModelProperty(value = "状态 0-停用 1-启用")
    private String status;

}