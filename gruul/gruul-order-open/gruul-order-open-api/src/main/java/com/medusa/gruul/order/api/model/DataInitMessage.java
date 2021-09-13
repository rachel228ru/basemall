package com.medusa.gruul.order.api.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 订单数据初始化消息体
 *
 * @author alan
 * @date 2019/10/6 14:08
 */
@Data
public class DataInitMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    private String shopId;
    private String tenantId;
}
