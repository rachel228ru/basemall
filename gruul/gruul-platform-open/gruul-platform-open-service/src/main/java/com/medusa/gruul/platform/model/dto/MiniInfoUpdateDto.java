package com.medusa.gruul.platform.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author whh
 */
@Data
public class MiniInfoUpdateDto {

    @ApiModelProperty(value = "操作类型 1-续费   4-禁用或开启")
    @NotNull
    private Integer optionType;
    @ApiModelProperty(value = "小程序Id")
    private Long miniId;

    @ApiModelProperty(value = "type == 1 服务时长,number类型 1 or 2")
    private Integer serviceTime;

    @ApiModelProperty(value = "type == 2 套餐id")
    private Long comboId;


    @ApiModelProperty(value = "type == 4 操作状态0-开启 1-已禁用")
    private Integer status;


}
