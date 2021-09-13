package com.medusa.gruul.logistics;

import com.medusa.gruul.common.core.monitor.EnableMonitorHeartbeat;
import com.medusa.gruul.common.swagger.annotation.EnableGruulSwagger2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;


/**
 * @author 赵峥
 */
@EnableScheduling
@EnableGruulSwagger2
@SpringCloudApplication
@EnableFeignClients(basePackages = {"com.medusa.gruul.*.*.feign"})
@EnableDiscoveryClient
@CrossOrigin
@RefreshScope
@EnableCaching
@MapperScan(basePackages = {"com.medusa.gruul.*.mapper"})
public class LogisticApplication{

    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        SpringApplication.run(LogisticApplication.class, args);
        System.out.println("启动成功,耗时: " + (System.currentTimeMillis() - time) / 1000);
    }

}