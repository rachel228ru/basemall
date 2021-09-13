package com.medusa.gruul.order.api.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 基础订单消息体
 *
 * @author alan
 * @date 2019/10/6 14:08
 */
@Data
@Accessors(chain = true)
public class BaseOrderMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long orderId;
    private String tenantId;
    private String shopId;

}
