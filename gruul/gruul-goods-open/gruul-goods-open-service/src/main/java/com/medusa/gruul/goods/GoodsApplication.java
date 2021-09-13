

package com.medusa.gruul.goods;

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
 * @Description: 商品服务
 * @Author: alan
 * @Date: 2019/9/2 22:34
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
public class GoodsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoodsApplication.class, args);
    }

}