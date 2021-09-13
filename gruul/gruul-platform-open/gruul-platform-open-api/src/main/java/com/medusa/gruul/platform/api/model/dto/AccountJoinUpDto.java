package com.medusa.gruul.platform.api.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 * @description
 * @data: 2020/1/11
 */
@Data
public class AccountJoinUpDto {

    /**
     * 小程序用户id
     */
    @ApiModelProperty("小程序店铺用户唯一id")
    private String userId;
    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String phone;
    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String passwd;
    /**
     * 角色编码
     */
    @ApiModelProperty("角色编码")
    private Integer joinType;

    /**
     * 租户id
     */
    @ApiModelProperty("租户id")
    private String tenantId;

}
