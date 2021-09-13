package com.medusa.gruul.goods.api.constant;

import com.medusa.gruul.common.redis.RedisVisitorBaseFacade;

/**
 * @description: 购物车商品redis key
 * @author: lcysike
 * @date: 2020/01/15
 */
public class GoodsProductRedisKey extends RedisVisitorBaseFacade {


    public static final String KEY_BASE = "Goods:Product:";

    public GoodsProductRedisKey() {
        this(KEY_BASE);
    }


    public GoodsProductRedisKey(String baseKey) {
        super(baseKey);
    }


}
