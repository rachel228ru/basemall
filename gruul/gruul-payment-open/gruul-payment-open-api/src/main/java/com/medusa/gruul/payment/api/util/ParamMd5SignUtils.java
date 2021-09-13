package com.medusa.gruul.payment.api.util;


import org.apache.commons.codec.digest.DigestUtils;


/**
 * @author create by zq
 * @date created in 2020/03/11
 */
public class ParamMd5SignUtils {


    static String MD5_KEY = "ParamMd5SignUtils";


    /**
     * MD5加密
     *
     * @param obj 明文
     * @return value
     */
    public static String md5(Object obj) {
        return DigestUtils.md5Hex(obj.toString() + MD5_KEY);
    }


    /**
     * MD5验证
     *
     * @param md5 密文
     * @return status
     */
    public static boolean checkParam(Object obj, String md5) {
        if(md5(obj).equalsIgnoreCase(md5)) {
            return true;
        }
        return false;
    }

}
