package com.medusa.gruul.payment.api.model.param.ips;


import lombok.Data;


/**
 * @author create by zq
 * @date created in 2019/11/10
 */
@Data
public class IpsCallBackResponse {


    public IpsCallBackResponse(String version, String ts, String merCode, String nonceStr, String format,
                            String encryptType, String data, String signType, String notifyType) {
        this.version = version;
        this.ts = ts;
        this.merCode = merCode;
        this.nonceStr = nonceStr;
        this.format = format;
        this.encryptType = encryptType;
        this.data = data;
        this.signType = signType;
        this.notifyType = notifyType;
    }


    /**
     * 版本号
     */
    private String version;


    /**
     * 时间戳
     */
    private String ts;


    /**
     * 商户号
     */
    private String merCode;


    /**
     * 随机字符串
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
     * 通知类型
     */
    private String notifyType;


    /**
     * 签名域
     */
    private String sign;


    /**
     * 业务域
     */
    private String data;


    /**
     * 签名类型
     */
    private String signType;

}