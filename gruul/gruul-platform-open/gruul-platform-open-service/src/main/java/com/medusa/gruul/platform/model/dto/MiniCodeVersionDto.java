package com.medusa.gruul.platform.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 * @description
 * @data: 2020/10/14
 */
@Data
public class MiniCodeVersionDto {

    /**
     * 关联子表id,只有更新的时候需要传入
     */
    @ApiModelProperty(value = "关联子表id,只有更新的时候需要传入")
    private Long id;

    /**
     * 小程序模版库中templateId
     */
    @ApiModelProperty(value = "小程序模版库中templateId")
    private String codeTempleteId;

    /**
     * 模版库中自定义版本号
     */
    @ApiModelProperty(value = "模版库中自定义版本号")
    private String codeTempleteVersion;

    /**
     * 版本说明
     */
    @ApiModelProperty(value = "版本说明")
    private String versionExplain;
}
