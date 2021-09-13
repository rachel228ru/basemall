

package com.medusa.gruul.common.log.annotation;

import java.lang.annotation.*;

/**
 * @Description: 操作日志注解
 * @Author: alan
 * @Date: 2019/7/13 19:34
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

	/**
	 * 描述
	 *
	 * @return {String}
	 */
	String value();
}
