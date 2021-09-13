
package com.medusa.gruul.common.log.event;

import com.medusa.gruul.common.log.entity.SysLogEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: 系统日志事件
 * @Author: alan
 * @Date: 2019/7/13 19:37
 */
@Getter
@AllArgsConstructor
public class SysLogEvent {
	private final SysLogEntity sysLog;
}
