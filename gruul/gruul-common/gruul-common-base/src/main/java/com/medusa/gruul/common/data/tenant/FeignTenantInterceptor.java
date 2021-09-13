

package com.medusa.gruul.common.data.tenant;

import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.util.CurUserUtil;
import com.medusa.gruul.common.dto.CurUserDto;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @Description: FeignTenantInterceptor.java
 * @Author: alan
 * @Date: 2019/7/13 19:32
 */
@Slf4j
public class FeignTenantInterceptor implements RequestInterceptor {
	@Override
	public void apply(RequestTemplate requestTemplate) {

		if (TenantContextHolder.getTenantId() == null) {
			log.error("TTL 中的 租户ID为空，feign拦截器 >> 增强失败");
			return;
		}
        requestTemplate.header(CommonConstants.TENANT_ID, TenantContextHolder.getTenantId());
        if (ShopContextHolder.getShopId() == null) {
            log.error("TTL 中的 商铺ID为空，feign拦截器 >> 增强失败");
            return;
        }
        requestTemplate.header(CommonConstants.SHOP_ID, ShopContextHolder.getShopId());
		CurUserDto dto= CurUserUtil.getHttpCurUser();
		if (dto==null){
			return;
		}
        if (StringUtils.isBlank(dto.getUserId())) {
			log.error("TTL 中的 用户为空，feign拦截器 >> 增强失败");
			return;
		}
        requestTemplate.header(CommonConstants.CUR_USER, CurUserUtil.getHttpCurUser().getUserId());
	}
}
