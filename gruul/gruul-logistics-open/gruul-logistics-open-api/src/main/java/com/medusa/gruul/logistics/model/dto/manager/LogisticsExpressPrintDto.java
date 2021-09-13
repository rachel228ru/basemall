package com.medusa.gruul.logistics.model.dto.manager;

import cn.hutool.core.bean.BeanUtil;
import com.medusa.gruul.logistics.api.entity.LogisticsExpressPrint;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 电子面单打印机设置
 *
 * @author lcysike
 */
@Data
@ApiModel(value = "LogisticsExpressPrintDto对象", description = "新增或修改电子面单打印机设置")
public class LogisticsExpressPrintDto {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键 id,新增不传,修改传原值")
    private Long id;

    @NotNull
    @ApiModelProperty(value = "设备类型")
    private String deviceType;

    @NotNull
    @ApiModelProperty(value = "设备型号")
    private String deviceModel;

    @NotNull
    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    @NotNull
    @ApiModelProperty(value = "打印机机身号")
    private String deviceNo;

    @NotNull
    @ApiModelProperty(value = "打印机KEY")
    private String deviceKey;

    @NotNull
    @ApiModelProperty(value = "状态 0-停用 1-启用")
    private String status;

    public LogisticsExpressPrint coverBean() {
        LogisticsExpressPrint logisticsExpressPrint = new LogisticsExpressPrint();
        BeanUtil.copyProperties(this, logisticsExpressPrint);
        return logisticsExpressPrint;
    }
}