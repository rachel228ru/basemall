package com.medusa.gruul.shops.api.enums;

import com.medusa.gruul.shops.api.constant.ExchangeConstant;
import com.medusa.gruul.shops.api.constant.QueueNameConstant;
import lombok.Getter;

/**
 * @author whh
 */
@Getter
public enum QueueEnum {

    /**
     * 用户中心初始化队列
     */
    QUEUE_SHOPS_CENTER_INIT(ExchangeConstant.SHOPS_EXCHANGE, QueueNameConstant.SHOPS_CENTER_QUEUE_INIT,
            QueueNameConstant.SHOPS_CENTER_QUEUE_INIT),
    /**
     * 店铺模板初始化队列
     */
    QUEUE_SHOPS_TEMPLATE_INIT(ExchangeConstant.SHOPS_EXCHANGE, QueueNameConstant.SHOPS_TEMPLATE_QUEUE_INIT,
            QueueNameConstant.SHOPS_TEMPLATE_QUEUE_INIT),

	QUEUE_SHOP_GUIDE_PAGE_INIT(ExchangeConstant.SHOPS_EXCHANGE, QueueNameConstant.SHOP_GUIDE_PAGE_QUEUE_INIT,
			QueueNameConstant.SHOP_GUIDE_PAGE_QUEUE_INIT),
	;



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

    QueueEnum(String exchange, String name, String routeKey) {
        this.exchange = exchange;
        this.name = name;
        this.routeKey = routeKey;
    }
}
