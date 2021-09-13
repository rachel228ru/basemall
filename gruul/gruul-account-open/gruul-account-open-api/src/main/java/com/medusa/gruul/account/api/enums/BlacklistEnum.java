package com.medusa.gruul.account.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author whh
 * @date 2019/12/03
 */

@Getter
@AllArgsConstructor
public enum BlacklistEnum {

    /**
     * 禁用类型
     */
    REJECT_COMMENT(2, "限制评论"),
    REJECT_ORDER(1, "限制下单");

    /**
     * 授权类型
     */
    private Integer type;

    /**
     * 类型名称
     */
    private String name;
}
