package com.medusa.gruul.sms.service;

import com.medusa.gruul.sms.dao.entity.TSmsOrderEntity;
import com.medusa.gruul.sms.model.dto.SendSmsDto;

import java.util.List;

/**
 * Copyright(C) 2019-12-22 17:32 美杜莎 Inc.ALL Rights Reserved.
 *
 * @version V1.0
 * @description: 美杜莎
 * @author: wangpeng
 * @date: 2019-12-22 17:32
 **/
public interface SmsOrderService {



   /***
   * @description:
   * @param:sendSmsDto
   * @return: void
   * @throws:
   * @author: wangpeng
   * @version : v1.0
   * @date: 2019/12/29 9:14 PM
   */
    int doCreateOrder(SendSmsDto sendSmsDto);

    List<TSmsOrderEntity> doListWaitSendOrder(int smsSendStatus);
}
