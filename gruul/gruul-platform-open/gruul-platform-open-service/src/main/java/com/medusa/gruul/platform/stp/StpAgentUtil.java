package com.medusa.gruul.platform.stp;

import cn.dev33.satoken.session.SaSession;

import java.util.Map;

/**
 * @author Huihuang Wu
 * @description 代理后台校验
 * @data: 2020/10/30
 */
public class StpAgentUtil {


    public static MeStpLogic stpLogic = new MeStpLogic("agent");

    public StpAgentUtil() {
    }

    public static String getTokenValue() {
        return stpLogic.getTokenValue();
    }

    public static String getTokenValueByLoginId(Object loginId) {
        return stpLogic.getTokenValueByLoginId(loginId);
    }

    public static Map<String, String> getTokenInfo() {
        return stpLogic.getTokenInfo();
    }

    public static void setLoginId(Object loginId) {
        stpLogic.setLoginId(loginId);
    }

    public static void logout() {
        stpLogic.logout();
    }

    public static void logoutByLoginId(Object loginId) {
        stpLogic.logoutByLoginId(loginId);
    }

    public static boolean isLogin() {
        return stpLogic.isLogin();
    }

    public static void checkLogin() {
        getLoginId();
    }

    public static Object getLoginId() {
        return stpLogic.getLoginId();
    }

    public static <T> T getLoginId(T defaultValue) {
        return stpLogic.getLoginId(defaultValue);
    }

    public static Object getLoginIdDefaultNull() {
        return stpLogic.getLoginIdDefaultNull();
    }

    public static String getLoginIdAsString() {
        return stpLogic.getLoginIdAsString();
    }

    public static int getLoginIdAsInt() {
        return stpLogic.getLoginIdAsInt();
    }

    public static long getLoginIdAsLong() {
        return stpLogic.getLoginIdAsLong();
    }

    public static Object getLoginIdByToken(String tokenValue) {
        return stpLogic.getLoginIdByToken(tokenValue);
    }

    public static SaSession getSessionByLoginId(Object loginId, boolean isCreate) {
        return stpLogic.getSessionByLoginId(loginId, isCreate);
    }

    public static SaSession getSessionByLoginId(Object loginId) {
        return stpLogic.getSessionByLoginId(loginId);
    }

    public static SaSession getSession() {
        return stpLogic.getSession();
    }

    public static boolean hasPermission(Object loginId, Object pcode) {
        return stpLogic.hasPermission(loginId, pcode);
    }

    public static boolean hasPermission(Object pcode) {
        return stpLogic.hasPermission(pcode);
    }

    public static void checkPermission(Object pcode) {
        stpLogic.checkPermission(pcode);
    }

    public static void checkPermissionAnd(Object... pcodeArray) {
        stpLogic.checkPermissionAnd(pcodeArray);
    }

    public static void checkPermissionOr(Object... pcodeArray) {
        stpLogic.checkPermissionOr(pcodeArray);
    }
}
