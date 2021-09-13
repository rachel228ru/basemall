package com.medusa.gruul.common.data.mybatis;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.medusa.gruul.common.data.tenant.GruulShopHandler;
import com.medusa.gruul.common.data.tenant.GruulTenantHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: MybatisPlusConfig.java
 * @Author: alan
 * @Date: 2019/7/13 19:21
 */
@Configuration
@ConditionalOnBean(DataSource.class)
@MapperScan("com.medusa.gruul.**.mapper")
public class MybatisPlusConfig {

	/**
	 * 创建租户维护处理器对象
	 *
	 * @return 处理后的租户维护处理器
	 */
	@Bean
	@ConditionalOnMissingBean
	public GruulTenantHandler gruulTenantHandler() {
		return new GruulTenantHandler();
	}

    /**
     * 创建商铺维护处理器对象
     *
     * @return 处理后的租户维护处理器
     */
    @Bean
    @ConditionalOnMissingBean
    public GruulShopHandler gruulShopHandler() {
        return new GruulShopHandler();
    }

	/**
	 * 分页插件
	 *
	 * @param gruulTenantHandler 租户处理器
	 * @return PaginationInterceptor
	 */
	@Bean
	@ConditionalOnMissingBean
    public MybatisPlusInterceptor paginationInterceptor(GruulTenantHandler gruulTenantHandler, GruulShopHandler gruulShopHandler) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        List<InnerInterceptor> interceptorList=new ArrayList<>();
        interceptorList.add(new TenantLineInnerInterceptor(gruulShopHandler));
        interceptorList.add(new TenantLineInnerInterceptor(gruulTenantHandler));
        //多租户插件
        interceptor.setInterceptors(interceptorList);
        //分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        //乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
		return interceptor;
	}
	
}
