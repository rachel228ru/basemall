package com.medusa.gruul.payment.api.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author create by zq
 * @date created in 2019/11/18
 */
@Getter
@AllArgsConstructor
public enum CheckNameEnum {

    /**
     * 不校验名称
     */
    NO_CHECK(0, "不校验名称"),


    /**
     * 强校验真实姓名
     */
    FORCE_CHECK(1, "强校验真实姓名");


    /**
     * 类型
     */
    private int type;

    /**
     * 描述
     */
    private String description;

}
