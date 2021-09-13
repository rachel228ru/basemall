package com.medusa.gruul.payment.api.model.message;

import com.medusa.gruul.payment.api.enums.PayChannelEnum;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * @description: 支付成功回调消息体
 * @author: alan
 * @date: 2019/11/30 10:49
 */

@Data
public class PaySuccessMessage {
    /**
     * 交易流水号
     */
    String tradeNo;
    /**
     * 支付渠道
     */
    PayChannelEnum payChannel;

    /**
     * 支付成功时间
     */
    LocalDateTime payTime;
}
