package com.medusa.gruul.goods.api.constant;

import lombok.Getter;

/**
 * @description: 消息队列枚举配置
 * @author: lcysike
 * @date: 2020/04/21 12:04
 */
@Getter
public enum GoodsQueueEnum {
    /**
     * 默认商品创建
     */
    DEFAULT_GOODS_CREATE(GoodsConstant.EXCHANGE_NAME, GoodsMyQueneName.GENERATE_DEFAULT_GOODS, "gruul.goods.default.create"),;
    /**
     * 交换名称
     */
    private String exchange;
    /**
     * 队列名称
     */
    private String name;
    /**
     * 路由键
     */
    private String routeKey;

    GoodsQueueEnum(String exchange, String name, String routeKey) {
        this.exchange = exchange;
        this.name = name;
        this.routeKey = routeKey;
    }

}
