package com.medusa.gruul.common.data.tenant;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.experimental.UtilityClass;

/**
 * <p>
 * 用户请求线程
 * </p>
 *
 * @author 王鹏
 * @since 2019-0911-23
 */
@UtilityClass
public class CurUserContextHolder {

    private final ThreadLocal<String> THREAD_LOCAL_CRU_USER = new TransmittableThreadLocal<>();

	/**
	 * 获取当前用户
	 *
	 * @return
	 */
    public String getCurUser() {
		return THREAD_LOCAL_CRU_USER.get();
	}

	/**
	 * 设置当前用户
	 *
	 * @param curUser
     *
	 */
    public void setCurUser(String curUser) {
		THREAD_LOCAL_CRU_USER.set(curUser);
	}

	public void clear() {
		THREAD_LOCAL_CRU_USER.remove();
	}
}
