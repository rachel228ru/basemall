package com.medusa.gruul.common.gray.feign;

import cn.hutool.core.util.StrUtil;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.util.WebUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: GrayFeignRequestInterceptor.java
 * @author: alan
 * @date: 2020/6/2 22:42
 */
@Slf4j
public class GrayFeignRequestInterceptor implements RequestInterceptor {
    /**
     * Called for every request. Add data using methods on the supplied {@link RequestTemplate}.
     *
     * @param template
     */
    @Override
    public void apply(RequestTemplate template) {
        String reqVersion = WebUtils.getRequest() != null
                ? WebUtils.getRequest().getHeader(CommonConstants.VERSION) : null;

        if (StrUtil.isNotBlank(reqVersion)) {
            log.debug("feign gray add header version :{}", reqVersion);
            template.header(CommonConstants.VERSION, reqVersion);
        }
    }
}

