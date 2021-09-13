package com.medusa.gruul.oss.task;

import cn.hutool.core.util.ObjectUtil;
import com.medusa.gruul.oss.api.constant.OssConfigRedisKey;
import com.medusa.gruul.oss.service.FileService;
import com.medusa.gruul.platform.api.model.dto.OssConfigDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @description: UpdateSkuTask.java
 * @author: alan
 * @date: 2019/11/22 21:41
 */
@Slf4j
@Component
public class UpdateConfigTask {

    @Resource
    private FileService fileService;


    @Scheduled(fixedRate = 1000 * 60 * 10)
    public void run() {
        log.info("-----------更新oss配置开始-----------");
        OssConfigDto ossConfigDto = fileService.getConfig();
        OssConfigRedisKey redisKey = new OssConfigRedisKey();
        log.info(ossConfigDto.toString());
        if (ObjectUtil.isNotNull(ossConfigDto)) {
            redisKey.setObject("base", ossConfigDto);
        }
        log.info("-----------更新oss配置结束-----------");
    }


}
