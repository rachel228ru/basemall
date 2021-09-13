package com.medusa.gruul.order.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 发货单签收消息体
 *
 * @author alan
 * @date 2019/10/6 14:08
 */
@Data
public class ReceiptSendBillOrderMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 发货单ID
     */
    private Long id;
    /**
     * 租户ID
     */
    private String tenantId;
    /**
     * 发货单包含的ID
     */
    @ApiModelProperty(value = "订单ID,必填")
    private List<Long> orderIds;

}
