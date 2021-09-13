package com.medusa.gruul.payment.handler;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.medusa.gruul.common.core.util.DateUtils;
import com.medusa.gruul.payment.api.constant.MagicConstant;
import com.medusa.gruul.payment.api.constant.StatusConstant;
import com.medusa.gruul.payment.api.entity.Payment;
import com.medusa.gruul.payment.api.entity.PaymentRecord;
import com.medusa.gruul.payment.api.model.dto.PayRequestDto;
import com.medusa.gruul.payment.api.model.dto.PayResultDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author whh
 */
public abstract class AbstractHandler implements PayMessageHandler {

    Logger logger = LoggerFactory.getLogger(getClass());

    public PayResultDto test(PayRequestDto payRequestDto) {
        return null;
    }

    public PaymentRecord generateRecod(PayRequestDto payRequestDto) {
        PaymentRecord paymentRecord = new PaymentRecord();
        paymentRecord.setRequestParams(JSON.toJSONString(payRequestDto));
        return paymentRecord;
    }


    public Payment generatePayment(PayRequestDto payRequestDto, long workerId, long datacenterId) {
        Payment payment = new Payment();
        payment.setTransactionId(IdUtil.getSnowflake(workerId, datacenterId).nextId());
        payment.setPayChannel(payRequestDto.getPayChannel());
        payment.setFeeType(payRequestDto.getFeeType());
        payment.setTotalFee(payRequestDto.getTotalFee());
        payment.setBody(payRequestDto.getBody());
        payment.setBusinessParams(payRequestDto.getBusinessParams());
        payment.setTerminalIp(StrUtil.isEmpty(payRequestDto.getTerminalIp()) ? MagicConstant.LOCAL_IP : payRequestDto.getTerminalIp());
        payment.setBusinessNotifyUrl(payRequestDto.getNotifyUrl());
        payment.setThirdPartyNotifyStatus(StatusConstant.ThirdpartyNotifyStatus.UNTREATED);
        payment.setThirdPartyNotifyNumber(0);
        payment.setBusinessNotifyStatus(StatusConstant.BusinessNotifyStatus.UNTREATED);
        payment.setTradeStatus(StatusConstant.TradeStatus.WAIT_BUYER_PAY);
        payment.setThirdPartyNotifyNumber(0);
        payment.setRouteKey(payRequestDto.getRouteKey());
        payment.setCreateTime(DateUtils.timestampCoverLocalDateTime(System.currentTimeMillis()));
        payment.setDeleted(false);
        DateTime endTime = setTimeoutExpress(payRequestDto.getTimeoutExpress());
        payment.setTimeoutExpress(DateUtils.timestampCoverLocalDateTime(endTime.getTime()));
        payment.setTenantId(payRequestDto.getTenantId());
        return payment;
    }

    /**
     *  m-分钟，h-小时，d-天， 1m 24h 1d
     * @param timeoutExpress 超时时间
     * @return
     */
    private DateTime setTimeoutExpress(String timeoutExpress) {
        DateTime endTime;
        if (StrUtil.isNotEmpty(timeoutExpress)) {
            int index = timeoutExpress.length() - 1;
            //获取数值
            Integer time = Integer.valueOf(StrUtil.subWithLength(timeoutExpress, 0, index));
            //获取单位
            String unit = StrUtil.subWithLength(timeoutExpress, index, 1);

            //m-分钟，h-小时，d-天，
            switch (unit) {
                case "m":
                    endTime = DateUtil.offsetMinute(DateUtil.date(), time);
                    break;
                case "h":
                    endTime = DateUtil.offsetHour(DateUtil.date(), time);
                    break;
                case "d":
                    endTime = DateUtil.offsetDay(DateUtil.date(), time);
                    break;
                default:
                    endTime = DateUtil.offsetMinute(DateUtil.date(), 15);
                    break;
            }
        } else {
            endTime = DateUtil.offsetMinute(DateUtil.date(), 15);
        }
        return endTime;
    }

}
