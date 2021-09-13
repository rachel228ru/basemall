package com.medusa.gruul.sms.service;

import com.medusa.gruul.sms.dao.entity.TSmsTemplateEntity;
import com.medusa.gruul.sms.model.dto.TemplateDto;

import java.util.List;

/**
 * Copyright(C) 2020-01-05 10:19 美杜莎 Inc.ALL Rights Reserved.
 *
 * @version V1.0
 * @description: 美杜莎
 * @author: wangpeng
 * @date: 2020-01-05 10:19
 **/
public interface SmsTempLateService {

    /***
    * @description: 添加模版
    * @param:templateDto
    * @return: int
    * @throws:
    * @author: wangpeng
    * @version : v1.0
    * @date: 2020/1/5 10:22 AM
    */
    int doAddTempLate(TemplateDto templateDto);

    /***
    * @description: 待推送审核模版
    * @param:status
    * @return: java.util.List<com.medusa.gruul.sms.dao.entity.TSmsOrderEntity>
    * @throws:
    * @author: wangpeng
    * @version : v1.0
    * @date: 2020/1/5 10:32 AM
    */
    List<TSmsTemplateEntity> doListWaitVerifyTempLate(long status);

    /***
    * @description: 短信模版推审
    * @param:smsOrderEntity
    * @return: void
    * @throws:
    * @author: wangpeng
    * @version : v1.0
    * @date: 2020/1/5 10:39 AM
    */
    void doVerify(TSmsTemplateEntity tSmsTemplateEntity);
}
