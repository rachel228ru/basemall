
package com.medusa.gruul.common.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 免登陆注解
 * @author wangpeng
 * @Date 2019年11月26日
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EscapeLogin {
	
	/**
	 *  token 参数名
	 *  @return
	 */
	String value() default "";

}
