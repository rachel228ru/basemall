package com.medusa.gruul.payment.api.feign;

import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.payment.api.entity.EntPay;
import com.medusa.gruul.payment.api.model.dto.*;
import com.medusa.gruul.payment.api.model.param.EntPayReQuestParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @author whh
 * @date 2019/11/06
 */
@FeignClient(value = "payment-open")
public interface RemotePaymentService {

    /**
     * 支付请求
     *
     * @param payRequestDto 请求数据
     * @return 请求结果
     */
    @RequestMapping(value = "/pay/request", method = RequestMethod.POST)
    PayResultDto payRequest(@RequestBody PayRequestDto payRequestDto);


    /**
     * 获取指定订单交易状态
     *
     * @param outTradeNo 业务订单号
     * @param transactionId
     * @param payChannel 支付渠道
     * @param tenantId   商户标识
     * @return 状态
     */
    @RequestMapping(value = "/pay/status/{outTradeNo}", method = RequestMethod.GET)
    PayStatusDto getPayStatus(@PathVariable("outTradeNo") String outTradeNo,
                              @PathVariable("transactionId") String transactionId,
                              @RequestParam("payChannel") String payChannel,
                              @RequestParam("tenantId") String tenantId);


    /**
     * 商户支付给个人请求
     *
     * @param param 请求数据
     * @return 请求结果
     */
    @RequestMapping(value = "/ent_pay/pay", method = RequestMethod.POST)
    EntPay payRequest(@RequestBody @Validated EntPayReQuestParam param);


    /**
     * 订单退款
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/pay/refund", method = RequestMethod.POST)
    Result payRefund(@RequestBody @Validated RefundRequestDto param);

}
