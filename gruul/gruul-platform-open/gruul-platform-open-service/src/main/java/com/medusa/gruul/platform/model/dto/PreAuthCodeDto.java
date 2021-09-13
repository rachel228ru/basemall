package com.medusa.gruul.platform.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author whh
 * @description
 * @data: 2020/3/11
 */
@Data
public class PreAuthCodeDto implements Serializable {

    private static final long serialVersionUID = -4499255503156003231L;

    @ApiModelProperty(value = "授权成功后跳转地址,前端地址")
    @NotBlank(message = "回调地址不能为空")
    private String successPage;

    @ApiModelProperty(value = "平台店铺id")
    @NotNull(message = "平台店铺id不能为空")
    private Long platformShopId;

    @ApiModelProperty(value = "要授权的帐号类型：1-公众号、2-小程序")
    @NotNull
    @Range(max = 2, min = 1)
    private Integer authType;

    /**
     * s1.0.7版本去除,重新授权时不需要指定原本小程序,可换绑其他小程序
     */
    @ApiModelProperty(value = "指定小程序或者公众号")
    @Deprecated
    private String appId;
}
