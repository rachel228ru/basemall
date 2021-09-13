

package com.medusa.gruul.common.log.event;

import com.medusa.gruul.common.log.entity.SysLogEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

/**
 * @Description: 异步监听日志事件
 * @Author: Administrator
 * @Date: 2019/7/8 21:38
 */
@Slf4j
@AllArgsConstructor
public class SysLogListener {


	@Async
	@Order
	@EventListener(SysLogEvent.class)
	public void saveSysLog(SysLogEvent event) {
		SysLogEntity sysLog = event.getSysLog();
		System.out.println(sysLog.toString());
	}
}
