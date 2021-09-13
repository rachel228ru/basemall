package com.medusa.gruul.afs.api.constant;

/**
 * The interface Afs queue name constant.
 *
 * @author alan
 * @description: OrderQueueNameConstant.java
 * @date 2019 /10/21 22:10
 */
public interface AfsQueueNameConstant {
    /**
     * 商家超时未审批自动通过申请
     */
    String AFS_MERCHANT_AUTO_CONFIRM = "gruul.afs.merchant.auto.confirm";
    /**
     * 用户超时未退货自动取消申请
     */
    String AFS_USER_RETURN_OVERTIME = "gruul.afs.user.return.overtime";
    /**
     * 订单已签收
     */
    String ORDER_RECEIPT = "gruul.afs.order_receipt";
    /**
     * 订单已发货
     */
    String ORDER_SHIPPED = "gruul.afs.order_shipped";
    /**
     * 订单已完成
     */
    String ORDER_COMPLETED = "gruul.afs.order_completed";

    /**
     * 发货单已经签收
     */
    String DELIVER_RECEIPT = "gruul.afs.deliver_receipt";

    /**
     * 售后数据初始化
     */
    String DATA_INIT = "gruul.afs.data.init";

}
