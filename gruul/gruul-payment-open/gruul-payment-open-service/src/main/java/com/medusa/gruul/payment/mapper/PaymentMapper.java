package com.medusa.gruul.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medusa.gruul.payment.api.entity.Payment;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author whh
 * @since 2019-11-04
 */
public interface PaymentMapper extends BaseMapper<Payment> {

    /**
     * 查询指定商户订单
     *
     * @param transactionId
     * @param outTradeNo    订单号
     * @param payChannel    渠道
     * @param tenantId      租户id
     * @return com.medusa.gruul.payment.api.entity.Payment
     */
    Payment selectOrderStatus(@Param("transactionId") String transactionId, @Param("outTradeNo") String outTradeNo, @Param("payChannel") String payChannel, @Param("tenantId") String tenantId);
}
