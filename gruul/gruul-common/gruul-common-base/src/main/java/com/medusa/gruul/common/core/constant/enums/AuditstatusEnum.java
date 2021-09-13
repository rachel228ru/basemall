package com.medusa.gruul.common.core.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 小程序审核状态
 *
 * @author whh
 */
@Getter
@AllArgsConstructor
public enum AuditstatusEnum {
    /**
     * 审核通过
     */
    PASS(0, "审核通过"),
    /**
     * 审核拒绝
     */
    REFUSE(1, "审核拒绝"),
    /**
     * 审核中
     */
    AUDIT(2, "审核中"),

    /**
     * 撤销
     */
    ANNUL(3, "撤销审核");

    /**
     * 类型
     */
    private int status;

    /**
     * 描述
     */
    private String description;
}
