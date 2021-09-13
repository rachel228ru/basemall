package com.medusa.gruul.payment.api.model.param.ips;


import lombok.Data;

import java.io.Serializable;


/**
 * @author create by zq
 * @date created in 2019/11/10
 */
@Data
public class IpsRequestPay implements Serializable {


	private static final long serialVersionUID = -7309728681154639375L;


	/**
     * version
     */
    private String version;


    /**
     * 时间
     */
    private String ts;


    /**
     * 商户
     */
    private String merCode;


    /**
     * 随机字符
     */
    private String nonceStr;


    /**
     * 数据格式
     */
    private String format;


    /**
     * 加密类型
     */
    private String encryptType;


    /**
     * 业务
     */
    private String data;


    /**
     * 签名类型
     */
    private String signType;


    /**
     * 签名
     */
    private String sign;


    @Override
    public String toString() {
        return "version=" + version + '&' +
                "ts=" + ts + '&' +
                "merCode=" + merCode + '&' +
                "nonceStr=" + nonceStr + '&' +
                "format=" + format + '&' +
                "encryptType=" + encryptType + '&' +
                "data=" + data + '&' +
                "signType=" + signType;
    }
}
