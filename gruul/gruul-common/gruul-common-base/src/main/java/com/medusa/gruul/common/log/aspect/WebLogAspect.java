package com.medusa.gruul.common.log.aspect;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.json.JSONUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.medusa.gruul.common.core.util.IPUtils;
import com.medusa.gruul.common.log.conf.IgnoreWebLogConfigs;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Description: 打印请求参数
 * @Author: alan
 * @Date: 2019/6/27 4:25 PM
 */

@Order(1)
@Slf4j
@Aspect
@Component
@EnableConfigurationProperties({IgnoreWebLogConfigs.class})
public class WebLogAspect {

    private final ThreadLocal<Boolean> THREAD_LOCAL_LOG = new TransmittableThreadLocal<>();

    private final List<String> headers= Arrays.asList("token","version","shopId","tenantId");


    @Autowired
    private IgnoreWebLogConfigs ignoreWebLogConfigs;

    /**
     * 以 controller 包下定义的所有请求为切入点
     */
    @Pointcut("execution(public * com.medusa.gruul..*.controller..*.*(..))")
    public void webLog() {
    }

    /**
     * 在切点之前织入
     *
     * @param proceedingJoinPoint
     * @throws Throwable
     */
    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        THREAD_LOCAL_LOG.set(Boolean.FALSE);
        TimeInterval timer = DateUtil.timer();
        // 开始打印请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Object result = proceedingJoinPoint.proceed();
        String name = proceedingJoinPoint.getSignature().getName();
        if (ignoreWebLogConfigs != null && CollectionUtil.isNotEmpty(ignoreWebLogConfigs.getIgnores())) {
            String[] classPath = proceedingJoinPoint.getTarget().getClass().getName().split("\\.");
            String className = classPath[classPath.length - 1];
            List<String> methods = ignoreWebLogConfigs.getIgnores().get(className);
            if(CollectionUtil.isNotEmpty(methods)){
                for (String method : methods) {
                    if (method.equals(name)) {
                        THREAD_LOCAL_LOG.set(Boolean.TRUE);
                        return result;
                    }
                }
            }
        }
        assert attributes != null;
        // 构建成一条长 日志，避免并发下日志错乱
        StringBuilder beforeReqLog = new StringBuilder(1000);
        // 日志参数
        List<Object> beforeReqArgs = new ArrayList<>();
        getLogArgs(request, beforeReqLog, beforeReqArgs, proceedingJoinPoint.getSignature(),
                proceedingJoinPoint.getArgs());

        // 打印出参
        beforeReqLog.append("Response Args  : {}\n");
        beforeReqArgs.add(JSONUtil.toJsonStr(result));
        // 执行耗时
        beforeReqLog.append("Time-Consuming : {} ms\n");
        beforeReqArgs.add(timer.interval());
        beforeReqLog.append(
                "=========================================== End ===========================================\n");
        log.info(beforeReqLog.toString(), beforeReqArgs.toArray());
        return result;
    }


    @AfterThrowing(pointcut = "webLog()", throwing = "e")
    public void doAfterThrow(JoinPoint joinPoint, Exception e) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        // 构建成一条长 日志，避免并发下日志错乱
        StringBuilder beforeReqLog = new StringBuilder(1000);
        // 日志参数
        List<Object> beforeReqArgs = new ArrayList<>();
        // 打印请求相关参数
        getLogArgs(request, beforeReqLog, beforeReqArgs, joinPoint.getSignature(), joinPoint.getArgs());
        beforeReqLog.append("Exception       : {}\n");
        beforeReqArgs.add(e.getLocalizedMessage());
        beforeReqLog.append(
                "=========================================== End ===========================================\n");
        log.error(beforeReqLog.toString(), beforeReqArgs.toArray());
    }


    private void getLogArgs(HttpServletRequest request, StringBuilder beforeReqLog, List<Object> beforeReqArgs,
                            Signature signature, Object[] args) {
        // 打印请求相关参数
        beforeReqLog.append("\n");
        beforeReqLog.append(
                "========================================== Start ==========================================\n");
        // 打印请求 url
        beforeReqLog.append("URL            : {} \n");
        beforeReqArgs.add(request.getRequestURL().toString());
        // 打印 Http method
        beforeReqLog.append("HTTP Method    : {}\n");
        beforeReqArgs.add(request.getMethod());

        // 打印调用 controller 的全路径以及执行方法
        beforeReqLog.append("Class Method   : {}.{}\n");
        beforeReqArgs.add(signature.getDeclaringTypeName());
        beforeReqArgs.add(signature.getName());
        // 打印请求的 IP
        beforeReqLog.append("IP             : {}\n");
        beforeReqArgs.add(IPUtils.getIpAddr(request));
        // 打印请求入参
        List<Object> objects = new ArrayList<>();
        for (Object arg : args) {
            boolean isResponse = (arg instanceof HttpServletResponse);
            boolean isRequest = (arg instanceof HttpServletRequest);
            if (!isRequest && !isResponse) {
                objects.add(arg);
            }
        }
        if (!objects.isEmpty()) {
            beforeReqLog.append("Request Args   : {}\n");
            beforeReqArgs.add(JSONUtil.toJsonStr(objects));
        }
        // 打印Headers
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            if (headers.contains(key)) {
                beforeReqLog.append("Header         : {}: {}\n");
                beforeReqArgs.add(key);
                String value = request.getHeader(key);
                beforeReqArgs.add(value);
            }
        }
    }


}
