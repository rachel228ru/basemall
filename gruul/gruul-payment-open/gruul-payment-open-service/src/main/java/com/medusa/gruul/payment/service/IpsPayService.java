package com.medusa.gruul.payment.service;


import com.medusa.gruul.payment.api.model.dto.PayRequestDto;
import com.medusa.gruul.payment.api.model.dto.PayResultDto;

import javax.servlet.http.HttpServletRequest;

/**
 * @author create by zq
 * @date created in 2019/11/18
 */
public interface IpsPayService {

    /**
     * IPS JSAPI PAY
     *
     * @param payRequestDto
     * @return PayResultDto
     */
    PayResultDto handle(PayRequestDto payRequestDto);


    /**
     * ips第三方回调入口
     *
     * @param request res
     * @return str
     */
    String ipsNotify(HttpServletRequest request);

}
