package com.medusa.gruul.platform.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author whh
 * @description
 * @data: 2020/3/9
 */
@Data
public class ConsoleUpdateDto {

    @ApiModelProperty(value = "店铺名称")
    @NotBlank(message = "店铺名称不能为空")
    private String shopName;

    @ApiModelProperty(value = "营业时间,自行分割")
    @NotBlank(message = "经营时间错误")
    private String businessHours;

    @ApiModelProperty(value = "店铺电话")
    private String shopPhone;

    @ApiModelProperty(value = "小程序底部打标")
    private String miniBottomLog;

    @ApiModelProperty(value = "店铺首页图片")
    private String logoUrl;
}
