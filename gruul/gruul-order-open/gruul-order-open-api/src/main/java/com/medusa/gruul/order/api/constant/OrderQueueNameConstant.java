package com.medusa.gruul.order.api.constant;

/**
 * OrderQueueNameConstant.java
 *
 * @author alan
 * @date 2019/10/21 22:10
 */
public interface OrderQueueNameConstant {


    /**
     * 初始化订单数据
     */
    String DATA_INIT = "gruul.order.data.init";
    /**
     * 生成订单
     */
    String ORDER_CREATE = "gruul.order.create";
    /**
     * 生成订单失败
     */
    String ORDER_CREATE_FAIL = "gruul.order.create.fail";
    /**
     * 订单已支付
     */
    String ORDER_PAYED = "gruul.order.payed";
    /**
     * 订单已发货
     */
    String ORDER_SHIPPED = "gruul.order.shipped";
    /**
     * 订单已签收
     */
    String ORDER_RECEIPT = "gruul.order.receipt";
    /**
     * 完成订单
     */
    String ORDER_COMPLETED = "gruul.order.completed";
    /**
     * 超时签收自动确认
     */
    String ORDER_AUTO_RECEIPT = "gruul.order.auto.receipt";
    /**
     * 超时未评价自动完成订单
     */
    String ORDER_AUTO_COMPLETED = "gruul.order.auto.completed";
    /**
     * 移出发货单
     */
    String DELIVER_REMOVE = "gruul.deliver.remove";
    /**
     * 取消订单
     */
    String ORDER_AUTO_CANCEL = "gruul.order.auto.cancel";
    /**
     * 关闭订单
     */
    String ORDER_CLOSE = "gruul.order.close";
    /**
     * 订单退款
     */
    String ORDER_RETURN = "gruul.order.return";
    /**
     * 取消订单失败
     */
    String ORDER_CANCEL_FAIL = "gruul.order.cancel.fail";
    /**
     * 支付回调
     */
    String PAYMENT_NOTIFY = "gruul.order.payment.notify";
    /**
     * 退款回调
     */
    String REFUND_NOTIFY = "gruul.order.refund.notify";


    /**
     * 退款回调成功
     */
    String REFUND_NOTIFY_SUCCEED = "gruul.order.refund.notify.succeed";

    //============================================物流模块============================================
    //Todo 阉割
    /**
     * 发货单生成
     */
    String DELIVER_CREATE = "gruul.deliver.create";


    /**
     * 发货单签收
     */
    String DELIVER_RECEIPT = "gruul.deliver.receipt";

}
