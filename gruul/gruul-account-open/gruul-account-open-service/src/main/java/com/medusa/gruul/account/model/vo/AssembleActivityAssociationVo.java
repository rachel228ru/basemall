package com.medusa.gruul.account.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 * @description
 * @data: 2020/4/11
 */
@Data
public class AssembleActivityAssociationVo {
    private static final long serialVersionUID = 4331728369625094037L;
    @ApiModelProperty(value = "头像url")
    private String avatarUrl;
    @ApiModelProperty(value = "用户名称")
    private String nikeName;
}
