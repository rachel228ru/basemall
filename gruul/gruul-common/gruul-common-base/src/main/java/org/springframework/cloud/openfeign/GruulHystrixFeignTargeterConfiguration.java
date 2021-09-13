

package org.springframework.cloud.openfeign;

import feign.hystrix.HystrixFeign;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @Description: HystrixFeignTargeter 配置
 * @Author: alan
 * @Date: 2019/8/10 17:16
 */
@Configuration
@ConditionalOnClass(HystrixFeign.class)
@ConditionalOnProperty("feign.hystrix.enabled")
public class GruulHystrixFeignTargeterConfiguration {

	@Bean
	@Primary
	public Targeter gruulFeignTargeter() {
		return new GruulHystrixTargeter();
	}
}
