package com.medusa.gruul.order.api.constant;

import lombok.Getter;

/**
 * 消息队列枚举配置
 *
 * @author alan
 * @date 2019/11/30 9:04
 */
@Getter
public enum OrderQueueEnum {
    /**
     * 创建订单
     */
    QUEUE_ORDER_CREATE(OrderConstant.EXCHANGE_NAME, OrderQueueNameConstant.ORDER_CREATE, "gruul.order.create"),
    /**
     * 创建订单失败
     */
    QUEUE_ORDER_CREATE_FAIL(OrderConstant.EXCHANGE_NAME, OrderQueueNameConstant.ORDER_CREATE_FAIL,
            "gruul.order.create.fail"),
    /**
     * 订单已支付
     */
    QUEUE_ORDER_PAYED(OrderConstant.EXCHANGE_NAME, OrderQueueNameConstant.ORDER_PAYED, "gruul.order.payed"),
    /**
     * 订单已发货
     */
    QUEUE_ORDER_SHIPPED(OrderConstant.EXCHANGE_NAME, OrderQueueNameConstant.ORDER_SHIPPED, "gruul.order.shipped"),
    /**
     * 订单已签收
     */
    QUEUE_ORDER_RECEIPT(OrderConstant.EXCHANGE_NAME, OrderQueueNameConstant.ORDER_RECEIPT, "gruul.order.receipt"),
    /**
     * 完成订单
     */
    QUEUE_ORDER_COMPLETED(OrderConstant.EXCHANGE_NAME, OrderQueueNameConstant.ORDER_COMPLETED, "gruul.order.completed"),
    /**
     * 超时签收自动确认
     */
    QUEUE_ORDER_AUTO_RECEIPT(OrderConstant.DELAY_EXCHANGE_NAME, OrderQueueNameConstant.ORDER_AUTO_COMPLETED,
            "gruul.order.auto.receipt"),
    /**
     * 超时未评价自动完成订单
     */
    QUEUE_ORDER_AUTO_COMPLETED(OrderConstant.DELAY_EXCHANGE_NAME, OrderQueueNameConstant.ORDER_AUTO_COMPLETED,
            "gruul.order.auto.completed"),
    /**
     * 移出发货单
     */
    QUEUE_DELIVER_REMOVE(OrderConstant.EXCHANGE_NAME, OrderQueueNameConstant.DELIVER_REMOVE,
            "gruul.order.deliver.remove"),
    /**
     * 取消订单
     */
    QUEUE_ORDER_AUTO_CANCEL(OrderConstant.DELAY_EXCHANGE_NAME, OrderQueueNameConstant.ORDER_AUTO_CANCEL,
            "gruul.order.auto.cancel"),
    /**
     * 关闭订单
     */
    QUEUE_ORDER_CLOSE(OrderConstant.EXCHANGE_NAME, OrderQueueNameConstant.ORDER_CLOSE, "gruul.order.close"),
    /**
     * 订单退款
     */
    QUEUE_ORDER_RETURN(OrderConstant.EXCHANGE_NAME, OrderQueueNameConstant.ORDER_RETURN, "gruul.order.return"),
    /**
     * 订单退款成功
     */
    QUEUE_ORDER_RETURN_SUCCEED(OrderConstant.EXCHANGE_NAME, OrderQueueNameConstant.REFUND_NOTIFY_SUCCEED,
            "gruul.order.refund.notify.succeed"),

    /**
     * 取消订单失败
     */
    QUEUE_ORDER_CANCEL_FAIL(OrderConstant.DELAY_EXCHANGE_NAME, OrderQueueNameConstant.ORDER_CANCEL_FAIL,
            "gruul.order.cancel.fail"),
    //================================物流模块==========================================================================

    /**
     * 发货单生成
     */
    QUEUE_DELIVER_CREATE(OrderConstant.DELIVER_EXCHANGE_NAME, "gruul.deliver.create", "gruul.deliver.create"),
    /**
     * 发货单签收成功
     */
    QUEUE_DELIVER_RECEIPT(OrderConstant.DELIVER_EXCHANGE_NAME, "gruul.deliver.receipt", "gruul.deliver.receipt"),
    //================================支付模块==========================================================================
    /**
     * 订单支付成功回调
     */
    QUEUE_ORDER_PAYMENT_NOTIFY(OrderConstant.PAYMENT_EXCHANGE, OrderQueueNameConstant.PAYMENT_NOTIFY, "gruul.order" +
            ".payment.notify"),
    /**
     * 订单退款成功回调
     */
    QUEUE_ORDER_REFUND_NOTIFY(OrderConstant.EXCHANGE_NAME, OrderQueueNameConstant.REFUND_NOTIFY,
            "gruul.order.refund.notify"),

    //================================控制台模块==========================================================================
    /**
     * 初始化订单设置
     */
    QUEUE_DATA_INIT(OrderConstant.ACCOUNT_EXCHANGE, OrderQueueNameConstant.DATA_INIT, "gruul.order.data.init"),


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

    OrderQueueEnum(String exchange, String name, String routeKey) {
        this.exchange = exchange;
        this.name = name;
        this.routeKey = routeKey;
    }

}
