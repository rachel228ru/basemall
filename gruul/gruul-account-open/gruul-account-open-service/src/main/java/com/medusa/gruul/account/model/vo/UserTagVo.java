package com.medusa.gruul.account.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 * @description
 * @data: 2019/11/19
 */
@Data
@ApiModel(value = "用户标签")
public class UserTagVo {

    @ApiModelProperty(value = "标签id")
    private Long tagId;


    @ApiModelProperty(value = "标签名")
    private String tagName;
}
