package com.medusa.gruul.payment.api.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单交易状态
 *
 * @author whh
 */
@Data
public class PayStatusDto {

    @ApiModelProperty("交易状态 0-无任何交易 交易状态：1（交易创建，等待买家付款）、2（未付款交易超时关闭）、3（交易支付成功）")
    public Integer tradeStatus;
}
