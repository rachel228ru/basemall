

package com.medusa.gruul.oss;

import com.medusa.gruul.common.swagger.annotation.EnableGruulSwagger2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@EnableGruulSwagger2
@SpringCloudApplication
@EnableFeignClients(basePackages = {"com.medusa.gruul.*.*.feign"})
@MapperScan("com.medusa.gruul.oss.dao")
public class OSSApplication {

	public static void main(String[] args) {
		SpringApplication.run(OSSApplication.class, args);
	}

}