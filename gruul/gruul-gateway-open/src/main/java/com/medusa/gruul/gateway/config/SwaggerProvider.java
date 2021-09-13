

package com.medusa.gruul.gateway.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 聚合接口文档注册
 * @Author: alan
 * @Date: 2019/7/18 21:02
 */
@Slf4j
@Component
@Primary
@AllArgsConstructor
public class SwaggerProvider implements SwaggerResourcesProvider {
	private static final String API_URI = "/v2/api-docs";
	private final RouteLocator routeLocator;

	private static SwaggerResource swaggerResource(String name, String location) {
		SwaggerResource swaggerResource = new SwaggerResource();
		swaggerResource.setName(name);
		swaggerResource.setLocation(location);
		swaggerResource.setSwaggerVersion("2.0");
		return swaggerResource;
	}

	@Override
	public List<SwaggerResource> get() {
		List<SwaggerResource> resources = new ArrayList<>();
		List<Route> routes = new ArrayList<>();
		routeLocator.getRoutes().subscribe(routes::add);
		for (Route route : routes) {
			if (!route.getId().contains("gateway") && !route.getId().contains("monitor")) {
				resources.add(swaggerResource(route.getId().replace("CompositeDiscoveryClient_", ""),
						route.getUri().toString().replace("lb://", "/").concat(API_URI)));
			}

		}
		return resources;
	}


}
