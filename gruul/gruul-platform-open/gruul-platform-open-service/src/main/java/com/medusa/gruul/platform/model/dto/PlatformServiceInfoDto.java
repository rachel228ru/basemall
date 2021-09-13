package com.medusa.gruul.platform.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author cqj
 */
@Data
public class PlatformServiceInfoDto {

    @ApiModelProperty(value = "服务说明")
    private String serviceDesc;

}
