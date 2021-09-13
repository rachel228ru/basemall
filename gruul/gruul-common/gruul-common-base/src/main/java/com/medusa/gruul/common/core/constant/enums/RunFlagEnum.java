package com.medusa.gruul.common.core.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 小程序运行状态
 *
 * @author whh
 */
@Getter
@AllArgsConstructor
public enum RunFlagEnum {

    /**
     * 0-未上传代码
     */
    NOT_UPLOADING(0, "未上传代码"),

    /**
     * 1-停止运行
     */
    RUN(1, "已上传代码");


    /**
     * 状态
     */
    private int status;

    /**
     * 描述
     */
    private String description;

}
