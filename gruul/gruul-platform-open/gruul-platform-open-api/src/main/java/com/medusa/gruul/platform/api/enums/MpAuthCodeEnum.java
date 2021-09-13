package com.medusa.gruul.platform.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 公众号权限集
 *
 * @author whh
 */
@Getter
@AllArgsConstructor
public enum MpAuthCodeEnum {

    /* 平台模块1000-2000  start================>*/
    MSG(1, "消息管理权限"),
    ACCOUNT_MANAGER(2, "用户管理权限"),
    ACCOUNT_SERVICE_PERMISSION(3, "帐号服务权限"),
    WEB_SERVICE_PERMISSION(4, "网页服务权限"),
    ACCOUNT_INFO_MOTIFY(5, "微信小店权限"),
    CUSTOMER_SERVICE(6, "微信多客服权限"),
    MASS_TEXTING(7, "群发与通知权限"),
    WECHAT_CARD_COUPON_PRIVILEGE(8, "微信卡券权限"),
    YIWUYIMA(9, "微信一物一码权限"),
    WIFI(10, "微信连 WI-FI 权限"),
    SUCAI(11, "素材管理权限"),
    ZHOUBIAN(12, "微信摇周边权限"),
    MENDIAN(13, "微信门店权限"),
    ZIDINGYI(15, "自定义菜单权限"),
    CHENGSHIFUWU(22, "城市服务接口权限"),
    DAINZIFAPIAO(24, "微信电子发票权限"),
    KAIFANGPINGTAIGUANLI(26, "微信开放平台帐号管理权限");

    /**
     * 类型
     */
    private Integer code;

    /**
     * 描述
     */
    private String description;

    public static MpAuthCodeEnum findAuthCode(Integer code) {
        for (MpAuthCodeEnum value : MpAuthCodeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }


}
