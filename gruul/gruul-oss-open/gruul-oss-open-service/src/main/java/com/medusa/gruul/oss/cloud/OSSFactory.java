package com.medusa.gruul.oss.cloud;


import com.medusa.gruul.common.core.constant.enums.CloudServiceEnum;
import com.medusa.gruul.oss.api.constant.OssConfigRedisKey;
import com.medusa.gruul.platform.api.model.dto.OssConfigDto;
import org.springframework.stereotype.Component;

/**
 * 文件上传Factory
 *
 * @author Mark sunlightcs@gmail.com
 */
@Component
public class OSSFactory {


    public AbstractCloudStorageService build() {
        //获取云存储配置信息
        OssConfigRedisKey redisKey = new OssConfigRedisKey();
        OssConfigDto dto = redisKey.getObject("base", new OssConfigDto());
        if (dto.getType().equals(CloudServiceEnum.QINIU.getType())) {
            return new QiniuCloudStorageService(dto);
        } else if (dto.getType().equals(CloudServiceEnum.ALIYUN.getType())) {
            return new AliyunCloudStorageService(dto);
        } else if (dto.getType().equals(CloudServiceEnum.QCLOUD.getType())) {
            return new QcloudCloudStorageService(dto);
        }
        return null;
    }

}
