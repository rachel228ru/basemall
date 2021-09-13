package com.medusa.gruul.platform.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author whh
 */
@ConfigurationProperties(prefix = "platform.admin")
@Data
public class PaltformProperties {

    /**
     * 超级账号
     */
    private String username;
    /**
     * 超级账号密码
     */
    private String password;

    /**
     * 超级账号密码
     */
    private String heartbeatUrl;
}
