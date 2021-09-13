package com.medusa.gruul.platform.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 * @description 公众号授权登录返回数据
 * @data: 2020/9/26
 */
@Data
public class AuthMpLoginVo {
    @ApiModelProperty(value = "用户h5可用token")
    private String token;
    @ApiModelProperty(value = "授权前给入的租户id")
    private String tenantId;
    @ApiModelProperty(value = "授权前给入的店铺id")
    private String shopId;
}
