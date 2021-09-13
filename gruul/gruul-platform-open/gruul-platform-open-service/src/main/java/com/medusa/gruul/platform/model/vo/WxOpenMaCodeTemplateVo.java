package com.medusa.gruul.platform.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 */
@Data
public class WxOpenMaCodeTemplateVo {

    @ApiModelProperty(value = "模板id")
    private Long templateId;

    @ApiModelProperty(value = "模版版本号(ide上传时)")
    private String userVersion;

    @ApiModelProperty(value = "模版描述(ide上传时)")
    private String userDesc;

    @ApiModelProperty(value = "开发者上传草稿时间 / 被添加为模版的时间(ide上传时)")
    private String createTime;
}
