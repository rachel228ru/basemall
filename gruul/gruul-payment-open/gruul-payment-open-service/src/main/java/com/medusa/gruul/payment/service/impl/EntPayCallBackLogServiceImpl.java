package com.medusa.gruul.payment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.payment.api.entity.EntPayCallBackLog;
import com.medusa.gruul.payment.mapper.EntPayCallBackLogMapper;
import com.medusa.gruul.payment.service.EntPayCallBackLogService;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;


/**
 * @author create by zq
 * @date created in 2019/11/18
 */
@Service(value = "entPayCallBackLogServiceImpl")
@Log
public class EntPayCallBackLogServiceImpl extends ServiceImpl<EntPayCallBackLogMapper, EntPayCallBackLog> implements EntPayCallBackLogService {
}
