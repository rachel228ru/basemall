package com.medusa.gruul.platform.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author cqj
 */
@Data
public class ServiceLibrariesDto {

    @ApiModelProperty(value = "业务基础库id,新增时为空")
    private String id;

    @ApiModelProperty(value = "*业务基础库名称")
    private String name;

    @ApiModelProperty(value = "业务基础库版本号")
    @NotBlank(message = "业务基础库版本号不能为空")
    private String version;

    @ApiModelProperty(value = "所属支撑基础库id")
    private Long belongId;

    @ApiModelProperty(value = "更新日志")
    private String remark;
}
