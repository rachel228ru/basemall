package com.medusa.gruul.order.api.model;

import com.medusa.gruul.common.dto.CurUserDto;
import lombok.Data;

import java.io.Serializable;

/**
 * 生成订单消息体
 *
 * @author alan
 * @date 2019/10/6 14:08
 */
@Data
public class CreateOrderMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private CreateOrderDto orderVo;
    private Long orderId;
    private CurUserDto curUser;
    private String tenantId;

}
