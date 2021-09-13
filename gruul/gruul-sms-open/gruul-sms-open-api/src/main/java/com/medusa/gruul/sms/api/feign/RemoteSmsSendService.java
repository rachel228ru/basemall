package com.medusa.gruul.sms.api.feign;

import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.sms.api.dto.SendSmsFeginDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Copyright(C) 2019-12-31 01:37 美杜莎 Inc.ALL Rights Reserved.
 *
 * @version V1.0
 * @description: 美杜莎
 * @author: wangpeng
 * @date: 2019-12-31 01:37
 **/
@FeignClient(value = "sms")
@ApiIgnore
public interface RemoteSmsSendService {



    /***
    * 发送短信
    * @param sendSmsFeginDto  短信信息
    * @return: java.lang.Boolean
    * @throws:
    * @author: wangpeng
    * @version : v1.0
    * @date: 2019/12/31 1:40 AM
    */
    @PostMapping("/sms-order/create/Order")
    Result createOrder(@RequestBody SendSmsFeginDto sendSmsFeginDto);
}
