
package com.medusa.gruul.shops;

import com.medusa.gruul.common.core.monitor.EnableMonitorHeartbeat;
import com.medusa.gruul.common.swagger.annotation.EnableGruulSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * @author create by zq
 * @date created in 2019/11/02
 */
@EnableGruulSwagger2
@SpringCloudApplication
@EnableFeignClients(basePackages = {"com.medusa.gruul.*.open.api.feign"})
@EnableDiscoveryClient
@EnableScheduling
public class ShopsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopsApplication.class, args);
    }

}