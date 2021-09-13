package com.medusa.gruul.platform.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author cqj
 */
@Data
public class LibrariesServiceListVo {

    @ApiModelProperty(value = "服务id")
    private Long id;

    @ApiModelProperty(value = "服务名称")
    private String name;

    @ApiModelProperty(value = "1正常  2-未运行服务 ")
    private Integer status;

    @ApiModelProperty(value = "当前运行服务数量")
    private Integer number;

    @ApiModelProperty(value = "版本号")
    private String version;

    @ApiModelProperty(value = "服务说明")
    private String description;

}
