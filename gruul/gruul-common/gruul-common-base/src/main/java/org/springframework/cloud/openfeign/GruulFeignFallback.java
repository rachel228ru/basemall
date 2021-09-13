package org.springframework.cloud.openfeign;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.api.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.common.core.util.SpringContextHolder;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @Description: fallback 代理处理
 * @Author: alan
 * @Date: 2019/8/10 17:28
 */
@Slf4j
@AllArgsConstructor
public class GruulFeignFallback<T> implements MethodInterceptor {
	private final Class<T> targetType;
	private final String targetName;
	private final Throwable cause;

	@Nullable
	@Override
	@SneakyThrows
	public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) {
		Class<?> returnType = method.getReturnType();
		if (Result.class != returnType) {
			return null;
		}
		FeignException exception = (FeignException) cause;

		byte[] content = exception.content();

		String str = StrUtil.str(content, StandardCharsets.UTF_8);

		log.error("GruulFeignFallback:[{}.{}] serviceId:[{}] message:[{}]", targetType.getName(), method.getName(),
				targetName, str);
		ObjectMapper objectMapper = SpringContextHolder.getBean(ObjectMapper.class);
		return objectMapper.readValue(str, R.class);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		GruulFeignFallback<?> that = (GruulFeignFallback<?>) o;
		return targetType.equals(that.targetType);
	}

	@Override
	public int hashCode() {
		return Objects.hash(targetType);
	}
}
