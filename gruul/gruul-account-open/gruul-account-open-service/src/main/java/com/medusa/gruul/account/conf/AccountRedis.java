package com.medusa.gruul.account.conf;

import com.medusa.gruul.common.redis.RedisVisitorBaseFacade;


/**
 * @author whh
 * @description
 * @data: 2019/11/26
 */
public class AccountRedis extends RedisVisitorBaseFacade {

    public static final String KEY_BASE = "account";


    public AccountRedis() {
        super(KEY_BASE);
    }

    public AccountRedis(String baseKey) {
        super(baseKey);
    }


}
