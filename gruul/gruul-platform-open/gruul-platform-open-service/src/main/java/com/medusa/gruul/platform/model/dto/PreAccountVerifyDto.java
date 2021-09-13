package com.medusa.gruul.platform.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author whh
 * @description
 * @data: 2020/1/17
 */
@Data
public class PreAccountVerifyDto {

    @ApiModelProperty(value = "使用的appId")
    @NotBlank(message = "未设置appId")
    private String appId;

    @ApiModelProperty(value = "回调成功后跳转地址,前端地址,回调成功之后会带上code参数,供查询一次回调结果")
    @NotBlank(message = "回调页面地址不能为空")
    private String redirectUrl;

    @ApiModelProperty(value = "当前租户对应的用户id,后端用前端不可见", hidden = true)
    private Long userId;

    @ApiModelProperty(value = "扫码场景 具体场景值查看ScanCodeScenesEnum枚举类")
    @NotBlank(message = "扫码场景值错误")
    private String scenes;

    @ApiModelProperty(value = "店铺id")
    private Long shopInfoId;
}
