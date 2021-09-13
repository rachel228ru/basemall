package com.medusa.gruul.sms.web.controller;

import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.sms.model.dto.TemplateDto;
import com.medusa.gruul.sms.service.SmsTempLateService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright(C) 2020-01-05 10:14 美杜莎 Inc.ALL Rights Reserved.
 *
 * @version V1.0
 * @description: 美杜莎
 * @author: wangpeng
 * @date: 2020-01-05 10:14
 **/

@RestController
@RequestMapping("/sms-tempLate")
public class SmsTempLateController {


    @Autowired
    private SmsTempLateService smsTempLateService;


    /**
     * @description:
     * @param:com.medusa.gruul.sms.model.dto.TemplateDto
     * @return: com.medusa.gruul.common.core.util.Result
     * @throws:
     * @author: wangpeng
     * @version : v1.0
     * @date: 2019/12/22 5:31 PM
     */
    @EscapeLogin
    @RequestMapping("/add/tempLate")
    @ApiOperation("添加模版")
    public Result addTempLate(@RequestBody TemplateDto templateDto){
        smsTempLateService.doAddTempLate(templateDto);
        return Result.ok();

    }
}
