package com.medusa.gruul.platform.conf;

/**
 * 魔法值常量(无法直接使用字符串,仅为去除p3c报错)
 *
 * @author whh
 */
public interface MeConstant {

    String LEVEL = "level";
    String ID = "id";
    String AES = "aes";
    String COMPONENT_VERIFY_TICKET = "component_verify_ticket";
    String AUTHORIZED = "authorized";
    String UNAUTHORIZED = "unauthorized";
    String UPDATEAUTHORIZED = "updateauthorized";
    String TEXT = "text";
    String WXD101A85AA106F53E = "wxd101a85aa106f53e";
    String WX570BC396A51B8FF8 = "wx570bc396a51b8ff8";

    /**
     * 布谷鸟appId
     */
    String BGN_APPID = "wxdb8a395db32a83df";
    String TESTCOMPONENT_MSG_TYPE_TEXT = "TESTCOMPONENT_MSG_TYPE_TEXT";
    String QUERY_AUTH_CODE = "QUERY_AUTH_CODE:";
    String EVENT = "event";
    String P12 = ".p12";
    String JSON = ".json";
    String EXCHANGE = "exchange";
    int STATUS_OK = 200;
    int STATUS_365 = 365;
    int STATUS_30 = 30;
    int STATUS_502 = 502;
    int STATUS_404 = 404;
    Integer STATUS_10000 = 10000;
    int MINI_CODE_80082 = -80082;
    int MINI_CODE_40029 = 40029;
    int MINI_CODE_61007 = 61007;
    int MINI_CODE_61004 = 61004;
    int WX_OPEN_CODE_89002 = 89002;
    String FUYI = "-1";
    String ZERO = "0.0";
    int FUER = -2;
    String WENHAO = "?";
    String DOUHAO = ",";
    String PARAM_KEY = "param_key";
    /**
     * 平台字符串
     */
    String PLATFORM = "platform";
    /**
     * jwt  私钥
     */
    String JWT_PRIVATE_KEY = "gruul-pc-platform";
}
