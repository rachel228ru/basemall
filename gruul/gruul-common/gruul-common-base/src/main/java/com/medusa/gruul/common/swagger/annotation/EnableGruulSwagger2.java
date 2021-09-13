
package com.medusa.gruul.common.swagger.annotation;

import com.medusa.gruul.common.swagger.config.SwaggerAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Description: 开启swagger
 * @Author: alan
 * @Date: 2019/7/21 10:05
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({SwaggerAutoConfiguration.class})
public @interface EnableGruulSwagger2 {
}
