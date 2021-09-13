package com.medusa.gruul.common.gray;

import com.medusa.gruul.common.gray.feign.GrayFeignRequestInterceptor;
import com.medusa.gruul.common.gray.rule.GrayRibbonLoadBalancerRule;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Slf4j
@Configuration
@ConditionalOnProperty(value = "gray.rule.enabled", havingValue = "true")
public class GrayRibbonLoadBalancerConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public GrayRibbonLoadBalancerRule ribbonLoadBalancerRule() {
        log.info("Initializing GrayRibbonLoadBalancerRule");
        return new GrayRibbonLoadBalancerRule();
    }

    @Bean
    public RequestInterceptor grayFeignRequestInterceptor() {
        log.info("Initializing GrayFeignRequestInterceptor");
        return new GrayFeignRequestInterceptor();
    }
}
