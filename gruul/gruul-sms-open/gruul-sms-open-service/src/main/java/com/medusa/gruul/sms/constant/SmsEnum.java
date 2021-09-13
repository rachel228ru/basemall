

package com.medusa.gruul.sms.constant;

import com.medusa.gruul.common.core.util.IResultCode;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统内置code
 *
 * @author L.cm
 */
@Getter
@AllArgsConstructor
@ApiModel(description = "系统内置code")
public enum SmsEnum implements IResultCode {
    //枚举相关
	SMS_TEMPLATE_NOT_EXIST(1001, "模版不存在或未通过审核"),
	SMS_SIGN_NOT_EXIST(1002, "签名不存在或未通过审核"),
	SMS_PROVIDER_NOT_EXIST(1003, "供应商不存在或未通过审核"),
	SMS_MOBILES_ERROR(1004, "号码异常"),
	;
	/**
	 * code编码
	 */
	final int code;
	/**
	 * 中文信息描述
	 */
	final String msg;
}
