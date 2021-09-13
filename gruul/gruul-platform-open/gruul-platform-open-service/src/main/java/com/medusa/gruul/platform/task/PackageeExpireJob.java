package com.medusa.gruul.platform.task;

import com.medusa.gruul.platform.service.IPlatformShopInfoService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author whh
 * @description 套餐过期检测
 * @data: 2020/8/26
 */
@JobHandler(value = "packageeExpireJob")
@Component
public class PackageeExpireJob extends IJobHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IPlatformShopInfoService shopInfoService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        shopInfoService.validateShopPastDue();
        return SUCCESS;
    }
}
