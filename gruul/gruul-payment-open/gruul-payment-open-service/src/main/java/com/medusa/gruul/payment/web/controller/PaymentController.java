package com.medusa.gruul.payment.web.controller;


import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.payment.service.IPaymentService;
import com.medusa.gruul.payment.service.IpsPayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author whh
 * @since 2019-11-04
 */
@RestController
@RequestMapping("/")
@Api(hidden = true)
public class PaymentController {

    @Autowired
    private IPaymentService paymentService;

    @Autowired
    private IpsPayService ipsPayService;


    /**
     * 第三方回调入口
     *
     * @param request res
     * @return str
     * @throws IOException io异常
     */
    @ApiOperation(value = "支付回调入口", hidden = true)
    @PostMapping(value = "/notify")
    @EscapeLogin
    public String notify(HttpServletRequest request) throws IOException {
        String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
        return paymentService.notify(xmlResult);
    }


    /**
     * ips第三方回调入口
     *
     * @param request res
     * @return str
     */
    @ApiOperation(value = "ips支付回调入口", hidden = true)
    @PostMapping(value = "/ips_notify")
    @EscapeLogin
    public String ipsNotify(HttpServletRequest request) {
        return ipsPayService.ipsNotify(request);
    }



    /**
     * 微信退款回调
     *
     * @param request
     * @throws IOException
     * @return str
     */
    @ApiOperation(value = "微信退款回调", hidden = true)
    @PostMapping(value = "/wx_refund_notify")
    @EscapeLogin
    public String wxPayRefundNotify(HttpServletRequest request) throws Exception {
        return paymentService.wxPayRefundNotify(request);
    }

}
