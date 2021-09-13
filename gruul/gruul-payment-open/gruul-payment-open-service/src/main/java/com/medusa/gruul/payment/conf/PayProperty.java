package com.medusa.gruul.payment.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author whh
 */
@Component
@ConfigurationProperties(prefix = "pay")
@Data
public class PayProperty {


    /**
     * 回调地址
     */
    private String notify;

    /**
     * 终端ID
     */
    private Integer workerId;

    /**
     * 数据中心ID
     */
    private Integer datacenterId;

    /**
     * ips终端ID
     */
    private Integer ipsWorkerId;

    /**
     * ips数据中心ID
     */
    private Integer ipsDatacenterId;

    /**
     * sxf终端ID
     */
    private Integer sxfWorkerId;

    /**
     * sxf数据中心ID
     */
    private Integer sxfDatacenterId;


    /**
     * IPS version
     */
    private String ipsVersion;


    /**
     * IPS ipsURL
     */
    private String ipsUrl;


    /**
     * IPS ipsTradePlatformPaySubUrl
     */
    private String ipsTradePlatformPaySubUrl;


    /**
     * ips回调地址
     */
    private String ipsNotify;


    /**
     * sxf 回调地址
     */
    private String sxfNotify;


    /**
     * sxf version
     */
    private String sxfVersion;


    /**
     * sxf sxfJsApiPayUrl
     */
    private String sxfJsApiPayUrl;


    /**
     * sft终端ID
     */
    private Integer sftWorkerId;

    /**
     * sft数据中心ID
     */
    private Integer sftDatacenterId;


    /**
     * sft 回调地址
     */
    private String sftNotify;


    /**
     * sft version
     */
    private String sftVersion;


    /**
     * sft sftJsApiPayUrl
     */
    private String sftJsApiPayUrl;

    /**
     * wx 退款回调url
     */
    private String wxRefundUrl;

}
