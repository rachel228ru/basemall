

package com.medusa.gruul.common.data.tenant;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: feign 租户信息拦截
 * @Author: alan
 * @Date: 2019/7/13 19:32
 */
@Configuration
public class FeignTenantConfiguration {
	@Bean
	public RequestInterceptor feignTenantInterceptor() {
		return new FeignTenantInterceptor();
	}
}
