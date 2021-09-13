package com.medusa.gruul.platform.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author whh
 * @description
 * @data: 2020/6/10
 */
@Data
public class GenerateMultipleDto {

    @ApiModelProperty(value = "多选默认值标识")
    private List<String> uniqueIdentifications;
    @ApiModelProperty(value = "店铺id")
    private String shopId;
}
