package com.medusa.gruul.order.api.constant;

import com.medusa.gruul.common.redis.RedisVisitorBaseFacade;

/**
 * 创建失败的订单redis key
 *
 * @author alan
 * @date 2019/10/22 21:27
 */
public class OrderFailedRedisKey extends RedisVisitorBaseFacade {


    public static final String KEY_BASE = "Order:Failed:";

    public OrderFailedRedisKey() {
        this(KEY_BASE);
    }


    public OrderFailedRedisKey(String baseKey) {
        super(baseKey);
    }


}
