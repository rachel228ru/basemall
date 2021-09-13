package com.medusa.gruul.common.data.tenant;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.experimental.UtilityClass;

/**
 * @Description: 租户工具类
 * @Author: alan
 * @Date: 2019/7/13 19:32
 */
@UtilityClass
public class TenantContextHolder {

    private final ThreadLocal<String> THREAD_LOCAL_TENANT = new TransmittableThreadLocal<>();

	/**
	 * 获取TTL中的租户ID
	 *
	 * @return
	 */
    public String getTenantId() {
        return THREAD_LOCAL_TENANT.get();
	}

	/**
	 * TTL 设置租户ID
	 *
	 * @param tenantId
	 */
    public void setTenantId(String tenantId) {
		THREAD_LOCAL_TENANT.set(tenantId);
	}

	public void clear() {
		THREAD_LOCAL_TENANT.remove();
	}
}
