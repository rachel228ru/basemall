package com.medusa.gruul.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 请求用户信息对象
 * </p>
 *
 * @author 王鹏
 * @since 2019-0911-23
 * @deprecated s1.3.1 标注过时，建议使用对应开发端的Dto
 */
@Data
@Deprecated
public class CurUserDto {
    /**
     * 店铺用户id或者平台用户id
     */
    private String userId;

    /**
     * 当前登录用户类型  0-小程序用户  1-平台用户  2-公众号账号
     */
    private Integer userType;

    /**
     * 商铺id
     */
    private String shopId;

    /**
     * 商铺类型
     */
    private Integer shopType;

    /**
     * 请求版本app
     */
    private String version;

    /**
     * 用户名称
     */
    private String nikeName;

    /**
     * 头像url
     */
    private String avatarUrl;

    /**
     * 性别 0：未知、1：男、2：女
     */
    private Integer gender;

    /**
     * 小程序openId 或 平台账号openId 或 公众号opnId
     */
    private String openId;


    /**
     * 当前pc用户所拥有的角色
     */
    private List<MiniRole> miniRoles;

    @Data
    public static class MiniRole {
        @ApiModelProperty(value = "角色名称")
        private String roleName;
        @ApiModelProperty(value = "角色编码")
        private String roleCode;
        @ApiModelProperty(value = "角色id")
        private Long roleId;
    }

}
