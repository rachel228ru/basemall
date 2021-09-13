package com.medusa.gruul.platform;

import cn.dev33.satoken.spring.SaTokenSetup;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.medusa.gruul.common.core.monitor.EnableMonitorHeartbeat;
import com.medusa.gruul.common.core.util.LocalDateTimeUtils;
import com.medusa.gruul.common.swagger.annotation.EnableGruulSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.time.LocalDateTime;

/**
 * @author whh
 */
@EnableGruulSwagger2
@SpringCloudApplication
@EnableFeignClients(basePackages = "com.medusa.gruul.*.api.feign")
@EnableDiscoveryClient
@SaTokenSetup
public class PlatformApplication {
    @Bean
    public MultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }

    public static void main(String[] args) {
        SpringApplication.run(PlatformApplication.class, args);
    }

}
//feat
//aaaa