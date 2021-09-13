package com.medusa.gruul.common.verify;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "gruul.tenant")
public class TenantConfigProperties {

    /**
     * 维护租户列名称
     */
    private String column = "tenant_id";
    /**
     * 维护商铺列名称
     */
    private String shop_column = "shop_id";

    /**
     * 是否使用商铺
     */
    private Boolean use_shop = true;

    /**
     * 多租户的数据表集合
     */
    private List<String> tables = new ArrayList<>();
}
