package com.medusa.gruul.payment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.payment.api.entity.PaymentRecord;
import com.medusa.gruul.payment.mapper.PaymentRecordMapper;
import com.medusa.gruul.payment.service.IPaymentRecordService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author whh
 * @since 2019-11-04
 */
@Service
public class PaymentRecordServiceImpl extends ServiceImpl<PaymentRecordMapper, PaymentRecord> implements IPaymentRecordService {

}
