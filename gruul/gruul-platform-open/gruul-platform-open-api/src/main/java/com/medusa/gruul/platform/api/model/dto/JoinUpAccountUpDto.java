package com.medusa.gruul.platform.api.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 接入账号修改数据dto
 *
 * @author whh
 * @description
 * @data: 2020/1/12
 */
@Data
public class JoinUpAccountUpDto {


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
     * 要修改的密码
     */
    @ApiModelProperty("要修改的密码")
    private String passwd;


    /**
     * 绑定的手机号
     */
    @ApiModelProperty("要修改的手机号")
    private String phone;


    /**
     * 角色编码
     */
    @ApiModelProperty("角色编码")
    private Integer roleCode;


    /**
     * 禁用状态   0-正常 1-禁用
     */
    @ApiModelProperty("禁用状态   0-正常 1-禁用")
    private Integer forbidStatus;


    /**
     * 操作类型  1-更新账号数据(手机号或密码) 2-移除指定角色  3-禁用或启用账号
     */
    @ApiModelProperty("操作类型  1-更新账号数据(手机号或密码) 2-移除指定角色  3-禁用或启用账号")
    private Integer option;
}
