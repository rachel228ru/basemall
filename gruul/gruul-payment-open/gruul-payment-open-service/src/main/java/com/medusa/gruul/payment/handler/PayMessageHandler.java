package com.medusa.gruul.payment.handler;

import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.payment.api.model.dto.PayRequestDto;
import com.medusa.gruul.payment.api.model.dto.PayResultDto;

/**
 * @author whh
 */
public interface PayMessageHandler {

    /**
     * 处理支付请求.
     *
     * @param payRequestDto 请求对象
     * @return
     * @throws ServiceException 异常
     */
    PayResultDto handle(PayRequestDto payRequestDto) throws ServiceException;

}
