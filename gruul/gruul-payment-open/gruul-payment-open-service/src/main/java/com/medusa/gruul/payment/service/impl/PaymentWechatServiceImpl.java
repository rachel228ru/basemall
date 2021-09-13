package com.medusa.gruul.payment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.payment.api.entity.PaymentWechat;
import com.medusa.gruul.payment.mapper.PaymentWechatMapper;
import com.medusa.gruul.payment.service.IPaymentWechatService;
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
public class PaymentWechatServiceImpl extends ServiceImpl<PaymentWechatMapper, PaymentWechat> implements IPaymentWechatService {

}
