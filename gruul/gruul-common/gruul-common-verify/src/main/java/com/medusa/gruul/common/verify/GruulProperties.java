package com.medusa.gruul.common.verify;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;


/**
 * gruul配置
 */
@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "gruul")
public class GruulProperties {

    /**
     * 授权码
     */
    private String authCode = "";
    /**
     * referer域名集合
     */
    private String referer = "";

}
