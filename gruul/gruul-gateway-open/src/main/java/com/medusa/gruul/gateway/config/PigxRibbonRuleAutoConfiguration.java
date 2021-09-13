

package com.medusa.gruul.gateway.config;


import com.medusa.gruul.common.core.support.RibbonRuleProperties;
import com.medusa.gruul.gateway.filter.GrayLoadBalancerClientFilter;
import com.medusa.gruul.gateway.rule.AbstractDiscoveryEnabledRule;
import com.medusa.gruul.gateway.rule.MetadataAwareRule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.alibaba.nacos.ribbon.NacosServer;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.config.LoadBalancerProperties;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Mica ribbon rule auto configuration.
 *
 * @author L.cm
 * @link https://github.com/lets-mica/mica
 */
@Configuration
@RequiredArgsConstructor
@ConditionalOnClass(NacosServer.class)
@AutoConfigureBefore(RibbonClientConfiguration.class)
@EnableConfigurationProperties(RibbonRuleProperties.class)
@ConditionalOnProperty(value = "ribbon.rule.enabled", matchIfMissing = true)
public class PigxRibbonRuleAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public AbstractDiscoveryEnabledRule metadataAwareRule() {
		return new MetadataAwareRule();
	}

	@Bean
	@ConditionalOnMissingBean(LoadBalancerClient.class)
	public LoadBalancerClient gruulLoadBalancerClient(SpringClientFactory springClientFactory) {
		return new RibbonLoadBalancerClient(springClientFactory);
	}

	@Bean
	@ConditionalOnMissingBean(LoadBalancerClientFilter.class)
	public LoadBalancerClientFilter grayLoadBalancerClientFilter(RibbonLoadBalancerClient gruulLoadBalancerClient
			, LoadBalancerProperties properties) {
		return new GrayLoadBalancerClientFilter(gruulLoadBalancerClient, properties);
	}

}
