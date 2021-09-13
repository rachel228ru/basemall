package com.medusa.gruul.platform.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * @author whh
 * @description
 * @data: 2020/5/23
 */
@Data
public class MotifyMsgStateDto {

    @ApiModelProperty(value = "id")
    @NotNull
    private Integer id;

    /**
     * 小程序订阅消息是否开启 0-关闭  1-开启
     */
    @ApiModelProperty(value = "小程序订阅消息是否开启 0-关闭  1-开启")
    @Range(min = 0, max = 1)
    private Integer miniOpen;

    /**
     * 短信模板消息是否开启 0-关闭  1-开启
     */
    @ApiModelProperty(value = "短信模板消息是否开启 0-关闭  1-开启")
    @Range(min = 0, max = 1)
    private Integer codeOpen;


    /**
     * 公众号是否发送  0关闭- 1-开启
     */
    @ApiModelProperty(value = "公众号是否发送  0关闭- 1-开启")
    @Range(min = 0, max = 1)
    private Integer mpOpen;

}
