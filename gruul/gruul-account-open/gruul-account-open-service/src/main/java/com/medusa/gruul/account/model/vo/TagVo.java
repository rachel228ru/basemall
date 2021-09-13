package com.medusa.gruul.account.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 * @description
 * @data: 2019/11/20
 */
@Data
public class TagVo {

    @ApiModelProperty(value = "标签名")
    private String tagName;

    @ApiModelProperty(value = "标签id")
    private Long tagId;

    @ApiModelProperty(value = "是否选中 false 或 trun(选中) ")
    private Boolean option;
}
