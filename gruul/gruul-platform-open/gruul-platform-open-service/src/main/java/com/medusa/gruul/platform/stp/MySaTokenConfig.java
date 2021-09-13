package com.medusa.gruul.platform.stp;

import cn.dev33.satoken.annotation.SaCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Huihuang Wu
 * @description
 * @data: 2020/10/30
 */
@Configuration
public class MySaTokenConfig implements WebMvcConfigurer {

    /**
     * 注册sa-token的拦截器，打开注解式鉴权功能
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 全局拦截器
        registry.addInterceptor(new SaCheckInterceptor(StpAgentUtil.stpLogic))
                .addPathPatterns("/agent/console/**")
                .excludePathPatterns(
                        //代理充值订单回调
                        "/agent/console/balance/notify/*"
                );
    }


}