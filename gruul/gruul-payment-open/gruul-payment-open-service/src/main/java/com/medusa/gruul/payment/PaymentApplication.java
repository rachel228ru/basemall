package com.medusa.gruul.payment;

import com.medusa.gruul.common.swagger.annotation.EnableGruulSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author whh
 */
@EnableGruulSwagger2
@SpringCloudApplication
@EnableFeignClients(basePackages = "com.medusa.gruul.*.api.feign")
@EnableDiscoveryClient
public class PaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentApplication.class, args);
    }

}