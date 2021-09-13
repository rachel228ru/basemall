package com.medusa.gruul.common.data.tenant;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.experimental.UtilityClass;

/**
 * @Description: 商铺工具类
 * @Author: alan
 * @Date: 2019/7/13 19:32
 */
@UtilityClass
public class ShopContextHolder {

    private final ThreadLocal<String> THREAD_LOCAL_SHOP = new TransmittableThreadLocal<>();

    /**
     * 获取TTL中的商铺ID
     *
     * @return
     */
    public String getShopId() {
        return THREAD_LOCAL_SHOP.get();
    }

    /**
     * TTL 设置商铺ID
     *
     * @param shopId
     */
    public void setShopId(String shopId) {
        THREAD_LOCAL_SHOP.set(shopId);
    }

    public void clear() {
        THREAD_LOCAL_SHOP.remove();
    }
}
