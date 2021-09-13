package com.medusa.gruul.order.api.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.swagger.annotations.ApiModel;
import lombok.Getter;

/**
 * 订单类型枚举
 *
 * @author alan
 * @date 2019/10/1 22:02
 */
@Getter
@ApiModel(value = "订单类型")
public enum OrderTypeEnum {

    /**
     * 商超订单
     */
    MALL(100, "商超订单"),
    /**
     * 秒杀订单
     */
    SEC_KILL(102, "秒杀订单"),
    /**
     * 换货单
     */
    EXCHANGE(103, "换货单"),
    /**
     * 补货单
     */
    REPLENISH(104, "补货单"),
    ;


    /**
     * 值
     */
    @EnumValue
    private final int code;
    /**
     * 描述
     */
    private final String desc;

    OrderTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static boolean isExchange(OrderTypeEnum status) {
        if (status == EXCHANGE
                || status == REPLENISH
        ) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

}
