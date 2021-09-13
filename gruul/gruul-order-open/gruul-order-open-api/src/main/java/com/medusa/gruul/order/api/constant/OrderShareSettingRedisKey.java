package com.medusa.gruul.order.api.constant;

import com.medusa.gruul.common.redis.RedisVisitorBaseFacade;

/**
 * 订单晒单设置 key
 *
 * @author alan
 * @date 2019/10/22 21:27
 */
public class OrderShareSettingRedisKey extends RedisVisitorBaseFacade {


    public static final String KEY_BASE = "Order:Share:Setting:";

    public OrderShareSettingRedisKey() {
        this(KEY_BASE);
    }


    public OrderShareSettingRedisKey(String baseKey) {
        super(baseKey);
    }


}
