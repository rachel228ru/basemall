package com.medusa.gruul.platform.model.dto.agent;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author whh
 * @description
 * @data: 2020/11/24
 */
@Data
public class AgentCreateShopDto {

    @ApiModelProperty(value = "店铺名称")
    @NotBlank(message = "店铺名称不能为空")
    private String shopName;

    @ApiModelProperty(value = "模板id t_platform_shop_template_info 模版id")
    @NotNull(message = "未选择模板")
    private Long shopTemplateId;

    @ApiModelProperty(value = "店铺首页图片")
    @NotBlank(message = "店铺首页图片不能为空")
    private String logoUrl;

    @ApiModelProperty(value = "商户id")
    @NotNull(message = "请选择用户")
    private Long accountId;
}
