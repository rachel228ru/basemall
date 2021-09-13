package com.medusa.gruul.afs.api.constant;

import lombok.Getter;

/**
 * The enum Afs queue enum.
 *
 * @author alan
 * @description: 消息队列枚举配置
 * @date 2019 /11/30 9:04
 */
@Getter
public enum AfsQueueEnum {

    /**
     * 超时未审批自动通过申请
     */
    QUEUE_AFS_MERCHANT_AUTO_CONFIRM(AfsConstant.DELAY_EXCHANGE_NAME, AfsQueueNameConstant.AFS_MERCHANT_AUTO_CONFIRM,
            AfsQueueNameConstant.AFS_MERCHANT_AUTO_CONFIRM),
    /**
     * 用户超时未退货自动取消申请
     */
    QUEUE_AFS_USER_RETURN_OVERTIME(AfsConstant.DELAY_EXCHANGE_NAME, AfsQueueNameConstant.AFS_USER_RETURN_OVERTIME,
            AfsQueueNameConstant.AFS_USER_RETURN_OVERTIME),
    /**
     * 初始化订单设置
     */
    QUEUE_DATA_INIT(AfsConstant.ACCOUNT_EXCHANGE, AfsQueueNameConstant.DATA_INIT, "gruul.afs.data.init"),


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

    AfsQueueEnum(String exchange, String name, String routeKey) {
        this.exchange = exchange;
        this.name = name;
        this.routeKey = routeKey;
    }

}
