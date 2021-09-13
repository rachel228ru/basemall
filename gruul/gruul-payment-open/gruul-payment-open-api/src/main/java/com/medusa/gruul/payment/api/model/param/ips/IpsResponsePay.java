package com.medusa.gruul.payment.api.model.param.ips;


import lombok.Data;


/**
 * @author create by zq
 * @date created in 2019/11/10
 */
@Data
public class IpsResponsePay {


    /**
     * 业务
     */
    private String data;


    /**
     * 签名
     */
    private String sign;


    /**
     * 响应代码 (000000:成功 ,999999:异常 )
     */
    private String respCode;


    /**
     * 响应描述
     */
    private String respMsg;

}