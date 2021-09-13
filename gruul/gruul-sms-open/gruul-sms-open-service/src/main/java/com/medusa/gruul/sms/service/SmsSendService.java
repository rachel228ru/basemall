package com.medusa.gruul.sms.service;

import com.medusa.gruul.sms.dao.entity.TSmsOrderEntity;

/**
 * Copyright(C) 2019-12-30 21:55 美杜莎 Inc.ALL Rights Reserved.
 *
 * @version V1.0
 * @description: 美杜莎
 * @author: wangpeng
 * @date: 2019-12-30 21:55
 **/
public interface SmsSendService {

    /***
    * @description: 短信发送
    * @param:com.medusa.gruul.sms.dao.entity.TSmsOrderEntity
    * @return: void
    * @throws:
    * @author: wangpeng
    * @version : v1.0
    * @date: 2019/12/30 10:01 PM
    */
    void sendSms(TSmsOrderEntity smsOrderEntity);
}
