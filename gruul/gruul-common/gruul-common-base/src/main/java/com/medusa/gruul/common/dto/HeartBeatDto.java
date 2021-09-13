package com.medusa.gruul.common.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author whh
 */
@Data
public class HeartBeatDto implements Serializable {

    /**
     * 业务基础库名称
     */
    private String businessName;

    /**
     * 版本
     */
    private String version;

    /**
     * 服务名称
     */
    private String applicationName;

    /**
     * 基础库类型  public-公众基础库   business-业务基础库
     */
    private String baseType;

    /**
     * 服务类型  0-公共服务  1-通用服务  2-定制服务
     */
    private String serviceType;

}
