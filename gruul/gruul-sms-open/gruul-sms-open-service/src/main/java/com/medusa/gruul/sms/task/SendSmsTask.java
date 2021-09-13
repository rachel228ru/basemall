package com.medusa.gruul.sms.task;

import cn.hutool.core.collection.CollectionUtil;
import com.medusa.gruul.sms.constant.SmsSmsStatus;
import com.medusa.gruul.sms.dao.entity.TSmsOrderEntity;
import com.medusa.gruul.sms.service.SmsOrderService;
import com.medusa.gruul.sms.service.SmsSendService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
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
public class SendSmsTask  {

    @Autowired
    private SmsOrderService smsOrderService;
    @Autowired
    private SmsSendService smsSendService;


    @Scheduled(fixedRate = 1000 * 5)
    public  String execute(){
        System.out.println("-----------短信发送任务开启-----------");
        log.info("-----------短信发送任务开启-----------");
        try {
            List<TSmsOrderEntity> data = getData();
            if (!CollectionUtil.isEmpty(data)) {
                data.forEach(smsOrderEntity -> {
                    smsSendService.sendSms(smsOrderEntity);
                });
            } else {
                log.info("-----------无待发送短信订单，跳过本次发送-----------");
            }

        } catch (Exception e) {

            log.error("短信发送异常", e);
        }

        log.info("-----------短信发送任务结束-----------");
        return "";
    };

    private List<TSmsOrderEntity> getData() {

        return smsOrderService.doListWaitSendOrder(SmsSmsStatus.WAIT_SEND);
    }


}
