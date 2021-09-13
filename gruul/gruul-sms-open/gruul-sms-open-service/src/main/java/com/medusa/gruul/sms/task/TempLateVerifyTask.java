package com.medusa.gruul.sms.task;

import cn.hutool.core.collection.CollectionUtil;
import com.medusa.gruul.sms.constant.SmsSmsStatus;
import com.medusa.gruul.sms.dao.entity.TSmsOrderEntity;
import com.medusa.gruul.sms.dao.entity.TSmsTemplateEntity;
import com.medusa.gruul.sms.service.SmsOrderService;
import com.medusa.gruul.sms.service.SmsSendService;
import com.medusa.gruul.sms.service.SmsTempLateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Copyright(C) 2019-12-29 22:38 美杜莎 Inc.ALL Rights Reserved.
 *
 * @version V1.0
 * @description: 美杜莎 短信发送job
 * @author: wangpeng
 * @date: 2019-12-29 22:38
 **/
@Slf4j
@Component
public class TempLateVerifyTask {

    @Autowired
    private SmsTempLateService smsTempLateService;


    @Scheduled(fixedRate = 1000 * 5)
    public  String execute(){
        log.info("-----------短信模版审核任务开启-----------");
        try {
            List<TSmsTemplateEntity> data = getData();
            if (!CollectionUtil.isEmpty(data)) {
                data.forEach(smsOrderEntity -> {
                    smsTempLateService.doVerify(smsOrderEntity);
                });
            } else {
                log.info("-----------无待审核模版，跳过本次发送-----------");
            }

        } catch (Exception e) {

            log.error("短信模版审核异常", e);
        }

        log.info("-----------短信模版审核任务结束-----------");
        return "";
    };

    private List<TSmsTemplateEntity> getData() {

        return smsTempLateService.doListWaitVerifyTempLate(0);
    }


}
