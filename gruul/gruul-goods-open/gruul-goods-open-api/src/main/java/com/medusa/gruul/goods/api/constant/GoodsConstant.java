package com.medusa.gruul.goods.api.constant;

/**
 * 商品常量
 *
 * @author lcysike
 */
public interface GoodsConstant {

    /**
     * 16位货号最小值
     */
    Long MIN_ID = 1000000000000000L;

    /**
     * 16位货号最大值
     */
    Long MAX_ID = 9999999999999999L;

    /**
     * 一小时的毫秒数
     */
    int SECOND_OF_HOUR = 3600000;

    /**
     * 一小时的毫秒数
     */
    int HOUR_OF_DAY = 24;

    /**
     * 订单模块默认交换机
     */
    String EXCHANGE_NAME = "gruul.goods.direct";

    /**
     * 商超专区
     */
    String SUPERMARKET_MODE = "SUPERMARKET";


    /**
     * 默认店铺tenantId
     */
    String DEFAULT_TENANT_ID = "100001";

    /**
     * 默认店铺shopId
     */
    String DEFAULT_SHOP_ID = "100001100001";

    /**
     * 测试店铺tenantId
     */
    String TEST_TENANT_ID = "100002";

    /**
     * 测试店铺shopId
     */
    String TEST_SHOP_ID = "100002100001";

    /**
     * 测试店铺shopId
     */
    String TEST_USER_TOKEN = "account:e0e8167bc903519c7dd7b7961d7e248ba41c7efb99c16c7d55667f8b0942d0e4";

}
