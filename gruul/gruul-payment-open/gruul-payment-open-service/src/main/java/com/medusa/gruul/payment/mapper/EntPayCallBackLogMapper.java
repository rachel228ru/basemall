package com.medusa.gruul.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medusa.gruul.payment.api.entity.EntPay;
import com.medusa.gruul.payment.api.entity.EntPayCallBackLog;
import org.springframework.stereotype.Repository;

/**
 * @author create by zq
 * @date created in 2019/11/18
 */
@Repository
public interface EntPayCallBackLogMapper extends BaseMapper<EntPayCallBackLog> {
}
