package org.springframework.cloud.openfeign;

import feign.Target;
import feign.hystrix.FallbackFactory;
import lombok.AllArgsConstructor;
import org.springframework.cglib.proxy.Enhancer;

/**
 * @Description: GruulFallbackFactory.java
 * @Author: alan
 * @Date: 2019/8/10 17:14
 */
@AllArgsConstructor
public class GruulFallbackFactory<T> implements FallbackFactory<T> {
	private final Target<T> target;

	@Override
	@SuppressWarnings("unchecked")
	public T create(Throwable cause) {
		final Class<T> targetType = target.type();
		final String targetName = target.name();
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(targetType);
		enhancer.setUseCache(true);
		enhancer.setCallback(new GruulFeignFallback<>(targetType, targetName, cause));
		return (T) enhancer.create();
	}
}
