package com.medusa.gruul.platform.api.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 * @description
 * @data: 2020/1/12
 */
@Data
public class MiniAuthCodeBindDto {

    /**
     * 平台用户id
     */
    @ApiModelProperty("平台用户id")
    private Long platformUserId;

    /**
     * 小程序用户id
     */
    @ApiModelProperty("小程序用户id")
    private String userId;

    /**
     * 授权code值
     */
    @ApiModelProperty("授权code值")
    private String code;


}
