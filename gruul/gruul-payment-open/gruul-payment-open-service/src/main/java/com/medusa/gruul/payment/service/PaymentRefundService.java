package com.medusa.gruul.payment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.payment.api.entity.PaymentRefund;
import com.medusa.gruul.payment.api.model.dto.RefundRequestDto;

/**
 * @author create by zq
 * @date created in 2019/11/18
 */
public interface PaymentRefundService extends IService<PaymentRefund> {


    /**
     * 退款
     *
     * @param param
     * @return result
     */
    Result payRefund(RefundRequestDto param);


}
