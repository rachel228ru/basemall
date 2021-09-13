package com.medusa.gruul.platform.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;


/**
 * @author whh
 */
@Data
@Deprecated
public class OldAccountInfoVo implements Serializable {

    private static final long serialVersionUID = 1036517078615766286L;

    @ApiModelProperty(value = "用户id")
    private Long id;

    @ApiModelProperty(value = "用户名称")
    private String nikeName;


    @ApiModelProperty(value = "头像url")
    private String avatarUrl;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "token")
    private String token;

    @ApiModelProperty(value = "邀请码")
    private String invitationCode;

    @ApiModelProperty(value = "当前登录的小程序信息")
    private MeMini miniInfos;

    @ApiModelProperty(value = "账号拥有的角色")
    private List<MiniRole> miniRoles;

    @ApiModelProperty(value = "拥有的菜单")
    private List<Menu> miniMenus;

    @ApiModelProperty(value = "当前模板版本跳转地址")
    private String backUrl;


    @Data
    public static class MeMini {
        @ApiModelProperty(value = "小程序信息id")
        private Long id;
        @ApiModelProperty(value = "小程序名称")
        private String miniName;
        @ApiModelProperty(value = "小程序appId")
        private String appId;
        @ApiModelProperty(value = "小程序头像")
        private String miniHeadIcon;
        @ApiModelProperty(value = "小程序租户id")
        private String tenantId;
        @ApiModelProperty(value = "商铺id")
        private String shopId;
    }

    @Data
    public static class MiniRole {
        @ApiModelProperty(value = "角色名称")
        private String roleName;
        @ApiModelProperty(value = "角色id")
        private Long roleId;
        @ApiModelProperty(value = "角色编号")
        private String roleCode;

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            MiniRole miniRole = (MiniRole) o;
            return roleCode.equals(miniRole.roleCode);
        }

        @Override
        public int hashCode() {
            return Objects.hash(roleCode);
        }
    }

    @Data
    public static class Menu {
        @ApiModelProperty(value = "标题")
        private String title;
        @ApiModelProperty(value = "路径")
        private String path;
        @ApiModelProperty(value = "name")
        private String name;
        @ApiModelProperty(value = "菜单id")
        private Long menuId;
        @ApiModelProperty(value = "父级id")
        private Long pId;
        @ApiModelProperty(value = "菜单图标")
        private String icon;
        @ApiModelProperty(value = "子菜单")
        private List<Menu> subMenu;
    }
}
