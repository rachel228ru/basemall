package com.medusa.gruul.sms.model.dto;

import lombok.Data;

/**
 * Copyright(C) 2020-01-01 12:07 美杜莎 Inc.ALL Rights Reserved.
 *
 * @version V1.0
 * @description: 美杜莎
 * @author: wangpeng
 * @date: 2020-01-01 12:07
 **/

@Data
public class SmsDoSendDto {

    private String smsOrderId;
    private String providerAppId;
    private String providerAppSecret;
    private String templateCode;
    private String smsTemplateContent;
    private String smsSign;
}
