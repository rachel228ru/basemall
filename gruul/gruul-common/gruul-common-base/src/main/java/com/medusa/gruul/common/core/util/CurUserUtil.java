package com.medusa.gruul.common.core.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.common.dto.CurMiniUserInfoDto;
import com.medusa.gruul.common.dto.CurPcUserInfoDto;
import com.medusa.gruul.common.dto.CurShopInfoDto;
import com.medusa.gruul.common.dto.CurUserDto;
import com.medusa.gruul.common.redis.RedisManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 请求用户信息工具类
 * </p>
 *
 * @author 王鹏
 * @since 2019-0911-23
 */
public class CurUserUtil {


    private CurUserUtil() {
    }

    /**
     * 注意判空
     * getCurUse
     *
     * @return com.medusa.gruul.common.dto.CurUserDto
     */
    public static CurUserDto getFeignCurUser() {
        CurUserDto curUserDto = null;
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null != requestAttributes) {
            HttpServletRequest request = requestAttributes.getRequest();
            String header = request.getHeader(CommonConstants.CUR_USER);
            if (StringUtils.isNotEmpty(header)) {
                curUserDto = JSON.parseObject(header, CurUserDto.class);
                return curUserDto;
            }
        }
        return curUserDto;
    }


    /**
     * 注意判空
     * getCurUse
     *
     * @return com.medusa.gruul.common.dto.CurUserDto
     * @deprecated s1.3.1 标注过时，建议使用对应开发端的Dto
     */
    @Deprecated
    public static CurUserDto getHttpCurUser() {
        CurUserDto curUserDto = null;
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null != requestAttributes) {
            HttpServletRequest request = requestAttributes.getRequest();
            String header = request.getHeader(CommonConstants.TOKEN);
            if (StringUtils.isNotEmpty(header)) {
                RedisManager redisManager = RedisManager.getInstance();
                String user = redisManager.get(header);
                curUserDto = JSON.parseObject(user, CurUserDto.class);
            }
        }
        return curUserDto;
    }


    /**
     * 获取当前线程变量中的租户id的店铺信息
     * getCurUse
     *
     * @return com.medusa.gruul.common.dto.CurUserDto
     */
    public static CurShopInfoDto getTenantIdShopInfo() {
        String tenantId = TenantContextHolder.getTenantId();
        if (StrUtil.isBlank(tenantId)) {
            throw new ServiceException("ThreadLocal中不存在有效的 tenantId");
        }
        RedisManager redisManager = RedisManager.getInstance();
        String redisValue = redisManager.get(CommonConstants.SHOP_INFO_REDIS_KEY.concat(":").concat(tenantId));
        if (StrUtil.isBlank(redisValue)) {
            throw new ServiceException("未获取到有效店铺信息");
        }
        return JSON.parseObject(redisValue, CurShopInfoDto.class);
    }

    /**
     * 获取小程序端（包括h5）请求用户信息
     * 数据源头为：小程序端登录时注入
     *
     * @return com.medusa.gruul.common.dto.CurUserDto
     */
    public static CurMiniUserInfoDto getMiniReqeustAccountInfo() {
        String currentRequestToken = CurUserUtil.getCurrentRequestToken(CommonConstants.ACCOUNT_TOKEN_PREFIX);
        RedisManager redisManager = RedisManager.getInstance();
        String redisValue = redisManager.get(CommonConstants.GLOBAL_REDIS_KEY.concat(currentRequestToken));
        return JSON.parseObject(redisValue, CurMiniUserInfoDto.class);
    }


    /**
     * 获取pc客户管理端请求用户信息（仅限客户后台，不包括平台管理端以及代理管理端）
     * 数据源头为：用户在pc端登录时注入
     *
     * @return com.medusa.gruul.common.dto.CurPcUserInfoDto
     */
    public static CurPcUserInfoDto getPcRqeustAccountInfo() {
        String currentRequestToken = CurUserUtil.getCurrentRequestToken(CommonConstants.PLATFORM_TOKEN_PREFIX);
        RedisManager redisManager = RedisManager.getInstance();
        String redisValue = redisManager.get(CommonConstants.GLOBAL_REDIS_KEY.concat(currentRequestToken));
        return JSON.parseObject(redisValue, CurPcUserInfoDto.class);
    }

    /**
     * 获取当前请求中的token
     * 如果带上指定标识，则会判断token是否包含指定标识
     *
     * @return java.lang.String
     */
    private static String getCurrentRequestToken(String mark) {
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null == requestAttributes) {
            throw new ServiceException("当前无法获取到请求");
        }
        String token = requestAttributes.getRequest().getHeader(CommonConstants.TOKEN);
        if (StringUtils.isBlank(token)) {
            throw new ServiceException("非有效请求");
        }
        if (StringUtils.isNoneBlank(mark) && !StrUtil.contains(token, mark)) {
            throw new ServiceException("非当前可用token");
        }
        return token;
    }
}
