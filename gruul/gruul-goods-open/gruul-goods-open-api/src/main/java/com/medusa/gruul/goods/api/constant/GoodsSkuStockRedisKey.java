package com.medusa.gruul.goods.api.constant;

import com.medusa.gruul.common.redis.RedisVisitorBaseFacade;

/**
 * @description: 商品sku库存redis key
 * @author: alan
 * @date: 2019/10/22 21:27
 */
public class GoodsSkuStockRedisKey extends RedisVisitorBaseFacade {


    public static final String KEY_BASE = "Goods:Sku:Stock:";

    public GoodsSkuStockRedisKey() {
        this(KEY_BASE);
    }


    public GoodsSkuStockRedisKey(String baseKey) {
        super(baseKey);
    }


}
