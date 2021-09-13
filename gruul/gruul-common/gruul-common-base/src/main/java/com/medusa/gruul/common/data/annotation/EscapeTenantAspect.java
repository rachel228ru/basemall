package com.medusa.gruul.common.data.annotation;


import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @description: EscapeShopAspect.aj
 * @author: alan
 * @date: 2020/2/16 21:04
 */
@Aspect
@Component
public class EscapeTenantAspect {


    /**
     * 注解EscapeShop的为切入点
     */
    @Pointcut("@annotation(com.medusa.gruul.common.data.annotation.EscapeTenant)")
    public void escapeShopPointCut() {
    }

    /**
     *  在切点之前织入
     * @return void
     * @author alan
     * @date 2020/2/16 21:22
     */
    @Before("escapeShopPointCut()")
    public void doBefore() {
        TenantContextHolder.setTenantId(CommonConstants.DEFAULT_TENANT_ID);
    }


}

