

package com.medusa.gruul.gateway.handler;

import com.medusa.gruul.common.core.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR;

/**
 * @Description: Hystrix 降级处理
 * @Author: alan
 * @Date: 2019/7/21 10:06
 */
@Slf4j
@Component
public class HystrixFallbackHandler implements HandlerFunction<ServerResponse> {
	@Override
	public Mono<ServerResponse> handle(ServerRequest serverRequest) {
		Optional<Object> originalUris = serverRequest.attribute(GATEWAY_ORIGINAL_REQUEST_URL_ATTR);

		originalUris.ifPresent(originalUri -> log.error("网关执行请求:{}失败,hystrix服务降级处理", originalUri));

		return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
			.contentType(MediaType.APPLICATION_JSON_UTF8)
				.body(BodyInserters.fromObject(Result.failed("服务异常")));
	}
}
