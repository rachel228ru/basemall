package com.medusa.gruul.platform.conf;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

/**
 * @author whh
 */
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = "wechat.redis")
@Data
public class RedisProperties extends JedisPoolConfig {
    private String host = Protocol.DEFAULT_HOST;
    private String port = "6379";
    private String password;
    private int database = 1;
    private int connectionTimeout = Protocol.DEFAULT_TIMEOUT;
    private int soTimeout = Protocol.DEFAULT_TIMEOUT;
    private String clientName;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        if (host == null || "".equals(host)) {
            host = Protocol.DEFAULT_HOST;
        }
        this.host = host;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if ("".equals(password)) {
            password = null;
        }
        this.password = password;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        if ("".equals(clientName)) {
            clientName = null;
        }
        this.clientName = clientName;
    }

}
