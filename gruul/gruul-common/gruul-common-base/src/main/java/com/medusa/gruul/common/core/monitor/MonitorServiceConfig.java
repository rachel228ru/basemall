package com.medusa.gruul.common.core.monitor;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Objects;

/**
 * 心跳监听配置文件
 *
 * @author cqj
 */
@ConfigurationProperties(prefix = "monitor")
@Data
public class MonitorServiceConfig {

    /**
     * 是否使用监听
     */
    private Boolean useRun;
    /**
     * 是否打印日子
     */
    private Boolean useLog;
    /**
     * 当前启动标识
     */
    private String startIdentifier;

    /**
     * 基础库唯一标识  不在nacos配置,从缓存中取
     */
    private String uniqueness;

    /**
     * 版本 不在nacos配置,从环境变量中取
     */
    private String version;

    /**
     * 服务名称
     */
    private String applicationName;

    /**
     * 所属基础库类型  public-支撑基础库   business-业务基础库
     */
    private String baseType;

    /**
     * 服务类型  universalService-通用服务  commissionService-定制服务
     */
    private String serviceType;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MonitorServiceConfig that = (MonitorServiceConfig) o;
        return uniqueness.equals(that.uniqueness) &&
                version.equals(that.version) &&
                applicationName.equals(that.applicationName) &&
                baseType.equals(that.baseType) &&
                serviceType.equals(that.serviceType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, applicationName, baseType, serviceType);
    }
}
