package com.medusa.gruul.payment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.medusa.gruul.payment.api.entity.Payment;
import com.medusa.gruul.payment.api.model.dto.PayStatusDto;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author whh
 * @since 2019-11-04
 */
public interface IPaymentService extends IService<Payment> {

    /**
     * 统一回调入口
     *
     * @param xmlResult xml
     * @return "" || success
     */
    String notify(String xmlResult);

    /**
     * 获取指定订单交易状态
     *
     * @param outTradeNo    业务订单号
     * @param payChannel    支付渠道
     * @param tenantId      商户标识
     * @param transactionId
     * @return 状态
     */
    PayStatusDto getPayStatus(String outTradeNo, String payChannel, String tenantId, String transactionId);


    /**
     * 回调函数共用
     *
     * @param payment
     * @param decryptData
     * @return
     */
    String innerHandleCallback(Payment payment, String decryptData);


    /**
     * 退款回调
     *
     * @param request
     * @throws Exception
     * @return string
     */
    String wxPayRefundNotify(HttpServletRequest request) throws Exception;

}
