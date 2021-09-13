package com.medusa.gruul.platform.constant;

import lombok.Getter;

/**
 * 公众号权限
 * @author whh
 */
@Getter
public enum MpPermissionEnum {

    /**
     *
     */
    MESSAGE_MANAGER_PERMISSION(1, "消息管理权限"),
    /**
     *
     */
    ACCOUNT_MANAGER_PERMISSION(2, "用户管理权限"),
    /**
     *
     */
    ACCOUNT_SERVICE_PERMISSION(3, "帐号服务权限"),
    /**
     *
     */
    WEB_SERVICE_PERMISSION(4, "网页服务权限"),
    /**
     *
     */
    WX_SHOP_PERMISSION(5, "微信小店权限"),
    /**
     *
     */
    WX_KF_PERMISSION(6, "微信多客服权限"),
    /**
     *
     */
    MASS_TEXTING_PERMISSION(7, "群发与通知权限"),
    /**
     *
     */
    COUPON_PERMISSION(8, "微信卡券权限"),
    /**
     *
     */
    YIWUYIM_PERMISSION(9, "微信一物一码权限"),
    /**
     *
     */
    WIFI_PERMISSION(10, "微信连 WI-FI 权限"),
    /**
     *
     */
    MATERIAL_PERMISSION(11, "素材管理权限"),
    /**
     *
     */
    SHARK_IT_OFF_PERMISSION(12, "微信摇周边权限"),
    /**
     *
     */
    OUTLET_PERMISSION(13, "微信门店权限"),
    /**
     *
     */
    CUSTOM_PERMISSION(15, "自定义菜单权限"),
    /**
     *
     */
    CITY_SERVICE_PERMISSION(22, "城市服务接口权限"),
    /**
     * 微信电子发票权限
     */
    ELECTRONIC_INVOICE_PERMISSION(26, "微信电子发票权限"),
    /**
     * 微信开放平台帐号管理权限
     */
    PLATFORM_MANAGER_PERMISSION(24, "微信开放平台帐号管理权限"),
    ;
    /**
     * 编码
     */
    private Integer code;
    /**
     * 描述
     */
    private String desc;

    MpPermissionEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
