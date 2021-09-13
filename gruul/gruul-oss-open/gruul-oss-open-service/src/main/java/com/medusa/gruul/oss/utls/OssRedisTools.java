package com.medusa.gruul.oss.utls;


import com.medusa.gruul.common.redis.RedisVisitorBaseFacade;


/**
 * @author create by zq
 * @date created in 2019/11/06
 */
public class OssRedisTools extends RedisVisitorBaseFacade {

    public static final String KEY_BASE = "oss";

    public OssRedisTools() {
        this(KEY_BASE);
    }

    public OssRedisTools(String baseKey) {
        super(baseKey);
    }

}
