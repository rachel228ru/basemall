package com.medusa.gruul.sms;

import com.medusa.gruul.common.swagger.annotation.EnableGruulSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableGruulSwagger2
@SpringCloudApplication
@EnableFeignClients(basePackages = "com.medusa.gruul.*.api.feign")
@EnableDiscoveryClient
@EnableScheduling

public class SmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmsApplication.class, args);
    }

}