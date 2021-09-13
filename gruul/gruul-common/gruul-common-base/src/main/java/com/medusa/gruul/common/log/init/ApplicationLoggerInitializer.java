package com.medusa.gruul.common.log.init;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @Description: 初始化日志路径
 * @Author: alan
 * @Date: 2019/7/13 19:37
 */
public class ApplicationLoggerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		ConfigurableEnvironment environment = applicationContext.getEnvironment();

		String appName = environment.getProperty("spring.application.name");

		String logBase = environment.getProperty("LOGGING_PATH", "logs");
		// spring boot admin 直接加载日志
		System.setProperty("logging.file", String.format("%s/%s/debug.log", logBase, appName));
	}
}
