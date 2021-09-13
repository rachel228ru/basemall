package com.medusa.gruul.common.core.monitor;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 心跳监听注解
 *
 * @author whh
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({MonitorAutoConfiguration.class})
public @interface EnableMonitorHeartbeat {
}
