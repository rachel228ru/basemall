package com.medusa.gruul.platform.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * @author whh
 * @description
 * @data: 2020/1/14
 */
@Data
public class MiniTesterDto {


    @ApiModelProperty(value = "微信号")
    private String wechatid;
    @ApiModelProperty(value = "操作类型  1-绑定 2-解除绑定")
    @Range(min = 1, max = 2, message = "类型选择错误")
    private Integer option;
}
