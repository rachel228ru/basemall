package com.medusa.gruul.platform.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author cqj
 */
@Data
public class BaseLibrariesVo {

    @ApiModelProperty(value = "基础库id")
    private Long id;

    @ApiModelProperty(value = "基础库名称")
    private String name;

    @ApiModelProperty(value = "0未运行服务  1正常 2异常，3已下线 ")
    private Integer status;

    @ApiModelProperty(value = "版本号")
    private String version;

    @ApiModelProperty(value = "所属支撑基础库版本")
    private String belongVersion;
    @ApiModelProperty(value = "所属支撑基础库版本id")
    private Long belongId;
    @ApiModelProperty(value = "服务总数量")
    private Integer totleCount;
    @ApiModelProperty(value = "当前活动数量")
    private Integer activetyCount;
    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "唯一标识")
    private String uniqueness;
}
