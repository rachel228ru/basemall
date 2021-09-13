package com.medusa.gruul.platform.conf;

import com.medusa.gruul.common.redis.RedisVisitorBaseFacade;

/**
 * @author whh
 * @description
 * @data: 2019/11/26
 */
public class PlatformRedis extends RedisVisitorBaseFacade {

    public static final String KEY_BASE = "platform";

    public PlatformRedis() {
        super(KEY_BASE);
    }
    public PlatformRedis(String baseKey) {
        super(baseKey);
    }
}
