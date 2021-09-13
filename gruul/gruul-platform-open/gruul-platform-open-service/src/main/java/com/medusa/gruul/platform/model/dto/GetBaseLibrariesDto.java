package com.medusa.gruul.platform.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author cqj
 */
@Data
public class GetBaseLibrariesDto {


    @ApiModelProperty(value = "搜索分类类型：1 业务基础库 2 支撑基础库")
    @NotNull
    private Integer categoryType;
}
