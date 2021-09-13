package com.medusa.gruul.account.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author whh
 * @description
 * @data: 2019/11/26
 */
@Component
@ConfigurationProperties(prefix = "snowflake")
@Data
public class SnowflakeProperty {

    /**
     * 终端ID
     */
    private Integer workerId;

    /**
     * 数据中心ID
     */
    private Integer datacenterId;
}
