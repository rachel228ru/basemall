package com.medusa.gruul.common.core.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 代码发布状态
 * @author whh
 */
@Getter
@AllArgsConstructor
public enum CodeReleaseState {
    /**
     * 未发布
     */
    NOT_RELEASE(0, "未发布"),
    /**
     * 已发布
     */
    release(1, "已发布");

    /**
     * 类型
     */
    private int status;

    /**
     * 描述
     */
    private String description;

}
