package com.medusa.gruul.goods.api.constant;

import com.medusa.gruul.common.redis.RedisVisitorBaseFacade;

/**
 * <p>
 * 商品缓存接口key值
 * </p>
 *
 * @author lcysike
 * @since 2019-11-19
 */
public class ShoppingCartRedisKey extends RedisVisitorBaseFacade {

    public static final String KEY_BASE = "Shopping:Cart:";

    public ShoppingCartRedisKey() {
        this(KEY_BASE);
    }

    public ShoppingCartRedisKey(String baseKey) {
        super(baseKey);
    }

}
