package com.medusa.gruul.oss.api.constant;

import com.medusa.gruul.common.redis.RedisVisitorBaseFacade;

/**
 * @description: oss配置
 * @author: alan
 * @date: 2019/10/22 21:27
 */
public class OssConfigRedisKey extends RedisVisitorBaseFacade {


    public static final String KEY_BASE = "Oss:Config";

    public OssConfigRedisKey() {
        this(KEY_BASE);
    }


    public OssConfigRedisKey(String baseKey) {
        super(baseKey);
    }


}

