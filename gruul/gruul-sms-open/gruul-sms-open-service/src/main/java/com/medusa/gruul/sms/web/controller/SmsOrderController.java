package com.medusa.gruul.sms.web.controller;

import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.sms.model.dto.SendSmsDto;
import com.medusa.gruul.sms.service.SmsOrderService;
import com.medusa.gruul.common.core.util.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright(C) 2019-12-22 17:07 美杜莎 Inc.ALL Rights Reserved.
 *
 * @version V1.0
 * @description: 美杜莎 短信收单
 * @author: wangpeng
 * @date: 2019-12-22 17:07
 **/


@RestController
@RequestMapping("/sms-order")
public class SmsOrderController {


    @Autowired
    private SmsOrderService authUserService;


    /**
    * @description:
    * @param:userAuthDto
    * @return: com.medusa.gruul.common.core.util.Result
    * @throws:
    * @author: wangpeng
    * @version : v1.0
    * @date: 2019/12/22 5:31 PM
    */
    @EscapeLogin
    @RequestMapping("/create/Order")
    @ApiOperation("创建短信发送工单")
    public Result createOrder(@RequestBody SendSmsDto sendSmsDto){

       authUserService.doCreateOrder(sendSmsDto);
      return Result.ok();

    }



}
