export const getAfterName = (type: string) => {
  return {
    // 提货点换货
    POINT_EXCHANGE: "重新发货",
    // 提货点退款
    POINT_REFUND: "退款",
    REFUND: "退款",
    EXCHANGE: "重新发货",
    RETURN_REFUND: "退货退款",
  }[type];
};

export const getAfterStatusName = (type: string) => {
  return {
    WAIT_FOR_BUSINESS_APPROVED: "待商家审核",
    WAIT_FOR_RETURN: "待退货",
    WAIT_FOR_BUSINESS_RECEIPT: "等待商家确认收货",
    WAIT_FOR_SEND: "待发货",
    WAIT_FOR_PICKUP: "待提货",
    WAIT_RECEIPT: "待签后",
    SUCCESS: "成功",
    CLOSE: "关闭",
    REFUNDED: "关闭",
    /**
     * 部分退款
     */
    PART_REFUNDED: "关闭",
    /**
     * 支付超时关闭
     */
    BUYER_PAY_TIMEOUT_CLOSE: "关闭",
    /**
     * 买家取消关闭
     */
    BUYER_CANCEL_CLOSE: "关闭",
    /**
     * 卖家取消关闭
     */
    SELLER_CANCEL_CLOSE: "关闭",
    /**
     * 换货成功关闭
     */
    EXCHANGE_SUCCESS_CLOSE: "关闭",
    /**
     * 换货关闭
     */
    EXCHANGE_CANCEL_CLOSE: "关闭",
  }[type];
};

export const getDetailAfterStatus = (type: string) => {
  return {
    WAIT_FOR_BUSINESS_APPROVED: "待商家审核",
    WAIT_FOR_RETURN: "待退货",
    WAIT_FOR_SEND: "待发货",
    WAIT_RECEIPT: "待签收",
    WAIT_FOR_PICKUP: "待提货",
    SUCCESS: "成功",
    CLOSE: "关闭",
  }[type];
};

export const isWaitReview = (type, aStatus, status) => {
  return (
    ["POINT_REFUND", "REFUND"].includes(type) &&
    status !== "WAIT_FOR_SEND" &&
    aStatus === "WAIT_FOR_BUSINESS_APPROVED"
  );
};

/**
 * 是否为待发货状态下申请退款
 * @param type 售后类型
 * @param status 订单售后状态
 * @param status 订单状态
 */
export const isWaitSendRefund = (type, aStatus, status) => {
  return (
    ["POINT_REFUND", "REFUND"].includes(type) &&
    status === "WAIT_FOR_SEND" &&
    aStatus === "WAIT_FOR_BUSINESS_APPROVED"
  );
};

/**
 * 退货
 * @param type 售后类型
 * @param status 订单售后状态
 * @param status 订单状态
 */
export const isExchange = (type, aStatus) => {
  return (
    ["POINT_EXCHANGE", "EXCHANGE"].includes(type) &&
    aStatus === "WAIT_FOR_BUSINESS_APPROVED"
  );
};

/**
 * 等待商家确认收货
 * @param type
 * @param aStatus
 */
export const isWaitConfirm = (type, aStatus) => {
  return (
    ["RETURN_REFUND"].includes(type) && aStatus === "WAIT_FOR_BUSINESS_RECEIPT"
  );
};

/**
 * 退货退款
 * @param type 售后类型
 * @param status 订单售后状态
 * @param status 订单状态
 */
export const isReturnAndrefund = (type, aStatus) => {
  return (
    ["RETURN_REFUND"].includes(type) && aStatus === "WAIT_FOR_BUSINESS_APPROVED"
  );
};
