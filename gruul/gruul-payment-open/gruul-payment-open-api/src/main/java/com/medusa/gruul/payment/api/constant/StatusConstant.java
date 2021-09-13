package com.medusa.gruul.payment.api.constant;

/**
 * @author whh
 * @date 2019/11/06
 */
public class StatusConstant {


    /**
     * 第三方回调状态 0未回调 1-已处理
     */
    public static class ThirdpartyNotifyStatus {
        public static Integer UNTREATED = 0;
        public static Integer PROCESSED = 1;
    }

    /**
     * 业务方是否已正确处理  0-未处理 1-已处理
     */
    public static class BusinessNotifyStatus {
        public static Integer UNTREATED = 0;
        public static Integer PROCESSED = 1;
    }

    /**
     * 交易状态：1（交易创建，等待买家付款）、2（未付款交易超时关闭）、3（交易支付成功）
     */
    public static class TradeStatus {
        public static Integer WAIT_BUYER_PAY = 1;
        public static Integer TRADE_CLOSED = 2;
        public static Integer TRADE_SUCCESS = 3;
    }
}
