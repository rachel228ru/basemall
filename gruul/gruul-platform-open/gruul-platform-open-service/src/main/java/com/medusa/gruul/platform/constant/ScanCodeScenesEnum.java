package com.medusa.gruul.platform.constant;

import lombok.Getter;

/**
 * @author whh
 * @description 扫码场景
 * @data: 2020-02-15
 */
@Getter
public enum ScanCodeScenesEnum {


    /**
     * 微信换绑
     */
    ACCOUNT_SWITCHING("AccountSwitching", "微信换绑"),
    /**
     * 账号注册
     */
    ACCOUNT_REGISTER("AccountRegister", "账号注册"),
    ACCOUNT_LOGGIN("AccountLoggin", "扫码登录"),
    ACCOUNT_SHOP_INFO_CHECK("AccountShopInfoCheck", "扫码校验用户是否是店铺拥有者"),
    ;
    private String scenes;
    private String desc;


    ScanCodeScenesEnum(String scenes, String desc) {
        this.scenes = scenes;
        this.desc = desc;
    }

    public static Boolean findScenes(String scenes) {
        for (ScanCodeScenesEnum value : ScanCodeScenesEnum.values()) {
            if (value.scenes.equals(scenes)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
}
