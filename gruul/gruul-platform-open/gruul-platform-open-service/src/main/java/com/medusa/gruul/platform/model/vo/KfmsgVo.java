package com.medusa.gruul.platform.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 * @description
 * @data: 2020/9/28
 */
@Data
public class KfmsgVo {

    @ApiModelProperty(value = "客服消息管理员openId")
    private String kfOpenIds;
    @ApiModelProperty(value = "客服消息模板id")
    private String kfmsgTemplateId;
    @ApiModelProperty(value = "开票消息管理员openId")
    private String invoiceOpenIds;
    @ApiModelProperty(value = "开票消息模板id")
    private String invoiceTemplateId;
}
