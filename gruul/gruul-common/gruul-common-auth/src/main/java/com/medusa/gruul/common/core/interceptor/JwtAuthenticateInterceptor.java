package com.medusa.gruul.common.core.interceptor;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;

import cn.hutool.json.JSONUtil;
import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.constant.enums.AuthCodeEnum;
import com.medusa.gruul.common.core.util.JwtUtils;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.common.redis.RedisManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class JwtAuthenticateInterceptor extends HandlerInterceptorAdapter {

    private final static Logger logger = LoggerFactory.getLogger(JwtAuthenticateInterceptor.class);
    private static final String ASSEMBLE_KEY = "Assemble:";
    private static final String AGENT_KEY = "platfrom:agent:";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            EscapeLogin escapeLogin = handlerMethod.getMethodAnnotation(EscapeLogin.class);
            if (escapeLogin != null) {
                return true;
            }
        }

        String token = request.getHeader(CommonConstants.TOKEN);
        if (StrUtil.isBlank(token)) {
            logger.error("URL:{} .认证失败", request.getRequestURL().toString());
            handleFailure(AuthCodeEnum.AUTH_FAIL.getType(), AuthCodeEnum.AUTH_FAIL.getDescription(), response);
            return false;
        }
        // moduleKey:JWT
//        String jwtReg = "[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_.+/=]*$";
//        String jwt = ReUtil.get(jwtReg, "account:eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MDE2OTY0OTN9.1aGs76XLS_FeL0wJnMu_XSeNP6bvbx_ellH6jwNv_FQ", 0);
//        if(StrUtil.isNotEmpty(jwt)){
//        }

        RedisManager redisManager =  RedisManager.getInstance();
        String user = redisManager.get(token);
        if (StringUtils.isBlank(user)) {
            if (StringUtils.isNotBlank(redisManager.get(ASSEMBLE_KEY + token))) {
                return super.preHandle(request, response, handler);
            }
            if (StringUtils.isNotBlank(redisManager.get(AGENT_KEY + token))) {
                return super.preHandle(request, response, handler);
            }
            handleFailure(AuthCodeEnum.AUTH_FAIL.getType(), AuthCodeEnum.AUTH_FAIL.getDescription(), response);
            return false;
        }

        return super.preHandle(request, response, handler);
    }

    private void handleFailure(int code, String msg, HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("utf8");
        PrintWriter out = response.getWriter();
        out.print(JSONUtil.toJsonStr(Result.failed(code, msg)));
        out.flush();
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    public static void main(String[] args) {
        String jwtReg = "[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_.+/=]*$";
        String jwt = ReUtil.get(jwtReg, "account:eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MDE2OTY0OTN9.1aGs76XLS_FeL0wJnMu_XSeNP6bvbx_ellH6jwNv_FQ", 0);
        System.out.println(new JwtUtils("grull",1).parseJwt(jwt));
    }
}
