package com.medusa.gruul.common.core.exception;

import com.medusa.gruul.common.core.util.IResultCode;
import lombok.NoArgsConstructor;

/**
 * @Description: ServiceException
 * @Author: alan
 * @Date: 2019/7/13 19:22
 */
@NoArgsConstructor
public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private String msg;
	private int code = 500;

	public ServiceException(String msg) {
		super(msg);
		this.msg = msg;
	}

    public ServiceException(IResultCode resultCode) {
        super(resultCode.getMsg());
        this.msg = resultCode.getMsg();
        this.code = resultCode.getCode();
    }

	public ServiceException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}

	public ServiceException(String msg, int code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}

	public ServiceException(String msg, int code, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}


}
