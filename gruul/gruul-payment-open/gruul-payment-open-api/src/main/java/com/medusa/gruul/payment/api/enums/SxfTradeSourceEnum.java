package com.medusa.gruul.payment.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author create by zq
 * @date created in 2019/11/10
 */
@Getter
@AllArgsConstructor
public enum SxfTradeSourceEnum {

    /**
     * 01服务商
     */
    SXF_SERVICE("01", "01服务商"),


    /**
     * 02收银台
     */
    SXF_CASHIER("02", "02收银台"),


    /**
     * 03硬件
     */
    SXF_HARDWARE("03", "03硬件");


    /**
     * 类型
     */
    private String type;


    /**
     * 描述
     */
    private String desc;

}
