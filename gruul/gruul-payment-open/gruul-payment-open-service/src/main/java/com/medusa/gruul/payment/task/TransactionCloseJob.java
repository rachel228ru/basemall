package com.medusa.gruul.payment.task;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.medusa.gruul.common.core.util.LocalDateTimeUtils;
import com.medusa.gruul.payment.api.constant.StatusConstant;
import com.medusa.gruul.payment.api.entity.Payment;
import com.medusa.gruul.payment.service.IPaymentService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 关闭超时订单
 *
 * @author whh
 */
@JobHandler(value = "transactionCloseJob")
@Component
public class TransactionCloseJob extends IJobHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IPaymentService paymentService;


    @Override
    public ReturnT<String> execute(String s) {
        logger.debug("执行定时任务");
        List<Payment> list = paymentService.list(
                new QueryWrapper<Payment>().lt("timeout_express", LocalDateTimeUtils.convertDateToLDT(new Date()))
                        .eq("trade_status", StatusConstant.TradeStatus.WAIT_BUYER_PAY));
        if (CollectionUtil.isNotEmpty(list)) {
            logger.debug("更新已过订单有效期的支付记录状态为交易关闭!");
            List<Long> idList = list.stream().map(Payment::getId).collect(Collectors.toList());
            Payment payment = new Payment();
            payment.setTradeStatus(StatusConstant.TradeStatus.TRADE_CLOSED);
            boolean flag = paymentService.update(payment, new QueryWrapper<Payment>().in("id", idList));
            logger.debug("需更新的ids: ".concat(idList.toString()));
            logger.debug("执行状态: ".concat(Boolean.toString(flag)));
        }
        return SUCCESS;
    }
}
