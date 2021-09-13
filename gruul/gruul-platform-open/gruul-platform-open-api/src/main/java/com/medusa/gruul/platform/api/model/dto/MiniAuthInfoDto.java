package com.medusa.gruul.platform.api.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 * @description
 * @data: 2020/4/3
 */
@Data
public class MiniAuthInfoDto {

    /**
     * 小程序accessToken
     */
    @ApiModelProperty(value = "小程序accessToken")
    private String accessToken;


    @ApiModelProperty(value = "小程序appId")
    private String appId;
}
