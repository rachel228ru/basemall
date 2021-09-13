package com.medusa.gruul.payment.web.remote;

import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.payment.api.entity.EntPay;
import com.medusa.gruul.payment.api.model.dto.RefundRequestDto;
import com.medusa.gruul.payment.api.model.param.EntPayReQuestParam;
import com.medusa.gruul.payment.service.EntPayService;
import com.medusa.gruul.payment.service.PaymentRefundService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @author create by zq
 * @date created in 2019/11/18
 */
@RestController
@RequestMapping("/")
@Api(tags = "商户对个人支付远程调用接口")
public class RemoteEntPayController {


    @Autowired
    private EntPayService entPayService;

    @Autowired
    private PaymentRefundService paymentRefundService;



    /**
     * 请求 商家对个人付款
     *
     * @param param
     * @return Result
     */
    @PostMapping("/ent_pay/pay")
    @EscapeLogin
    @ApiOperation(value = "请求 商家对个人付款")
    public EntPay pay(@RequestBody @Validated EntPayReQuestParam param) {
        return entPayService.pay(param);
    }


    /**
     * 订单退款
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "订单退款")
    @PostMapping(value = "/pay/refund")
    @EscapeLogin
    Result payRefund(@RequestBody @Validated RefundRequestDto param) {
        return paymentRefundService.payRefund(param);
    }

}
