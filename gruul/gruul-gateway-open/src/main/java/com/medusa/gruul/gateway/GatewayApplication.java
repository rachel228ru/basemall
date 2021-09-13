

package com.medusa.gruul.gateway;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @Description: 网关应用
 * @Author: alan
 * @Date: 2019/7/17 20:38
 */
@SpringCloudApplication
@EnableAutoConfiguration
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}
}
