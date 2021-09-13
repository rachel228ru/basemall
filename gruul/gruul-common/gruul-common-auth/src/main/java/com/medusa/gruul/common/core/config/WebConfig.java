package com.medusa.gruul.common.core.config;

import com.medusa.gruul.common.core.interceptor.JwtAuthenticateInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtAuthenticateInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/swagger-ui.html")
                .excludePathPatterns("/webjars/**")
                .excludePathPatterns("/swagger/**")
                .excludePathPatterns("/v2/api-docs")
                .excludePathPatterns("/swagger-resources/**")
                .excludePathPatterns("/actuator/**")
                .excludePathPatterns("/error")
                .excludePathPatterns("/broadcast")
                .excludePathPatterns("/sockjs/broadcast")
        ;
    }
}
