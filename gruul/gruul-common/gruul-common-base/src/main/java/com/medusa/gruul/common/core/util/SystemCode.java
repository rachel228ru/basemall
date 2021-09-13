

package com.medusa.gruul.common.core.util;

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
public enum SystemCode implements IResultCode {
	/**
	 * 系统未知异常
	 */
	FAILURE(SystemCode.FAILURE_CODE, "系统未知异常"),
	/**
	 * 操作成功
	 */
	SUCCESS(SystemCode.SUCCESS_CODE, "操作成功"),
	/**
	 * 用户信息已过期
	 */
    UNAUTHORIZED(SystemCode.UNAUTHORIZED_CODE, "用户信息已过期，请重新登陆"),
	/**
	 * 缺少必要的请求参数
	 */
	PARAM_MISS(SystemCode.PARAM_MISS_CODE, "缺少必要的请求参数"),
	/**
	 * 请求参数类型错误
	 */
	PARAM_TYPE_ERROR(SystemCode.PARAM_TYPE_ERROR_CODE, "请求参数类型错误"),
	/**
	 * 请求参数绑定错误
	 */
	PARAM_BIND_ERROR(SystemCode.PARAM_BIND_ERROR_CODE, "请求参数绑定错误"),
	/**
	 * 参数校验失败
	 */
	PARAM_VALID_ERROR(SystemCode.PARAM_VALID_ERROR_CODE, "参数校验失败"),
	/**
	 * 404 没找到请求
	 */
	NOT_FOUND(SystemCode.NOT_FOUND_CODE, "404 没找到请求"),
	/**
	 * 消息不能读取
	 */
	MSG_NOT_READABLE(SystemCode.MSG_NOT_READABLE_CODE, "消息不能读取"),
	/**
	 * 不支持当前请求方法
	 */
	METHOD_NOT_SUPPORTED(SystemCode.METHOD_NOT_SUPPORTED_CODE, "不支持当前请求方法"),
	/**
	 * 不支持当前媒体类型
	 */
	MEDIA_TYPE_NOT_SUPPORTED(SystemCode.MEDIA_TYPE_NOT_SUPPORTED_CODE, "不支持当前媒体类型"),
	/**
	 * 不接受的媒体类型
	 */
	MEDIA_TYPE_NOT_ACCEPT(SystemCode.MEDIA_TYPE_NOT_ACCEPT_CODE, "不接受的媒体类型"),
	/**
	 * 请求被拒绝
	 */
	REQ_REJECT(SystemCode.REQ_REJECT_CODE, "请求被拒绝"),

	//-------------------------------------------------------------//
	/**
	 * 数据不存在
	 */
	DATA_NOT_EXIST(SystemCode.DATA_NOT_EXIST_CODE, "数据不存在"),
	/**
	 * 数据已存在
	 */
	DATA_EXISTED(SystemCode.DATA_EXISTED_CODE, "数据已存在"),
	/**
	 * 数据添加失败
	 */
	DATA_ADD_FAILED(SystemCode.DATA_ADD_FAILED_CODE, "数据添加失败"),
	/**
	 * 数据更新失败
	 */
	DATA_UPDATE_FAILED(SystemCode.DATA_UPDATE_FAILED_CODE, "数据更新失败"),
	/**
	 * 数据删除失败
	 */
	DATA_DELETE_FAILED(SystemCode.DATA_DELETE_FAILED_CODE, "数据删除失败"),


    //-------------------------------------------------------------//
    /**
     * 商品已经售罄
     */
    ITEM_SOLD_OUT(SystemCode.ITEM_SOLD_OUT_CODE, "商品已经售罄"),





	;

    /**
     * 微信第三方平台异常 code
     */
    public static final int  WX_PLATEFROM_EXCEPTION = 101000;


	/**
	 * 通用 异常 code
	 */
	public static final int FAILURE_CODE = 400;
	public static final int SUCCESS_CODE = 200;
	public static final int UNAUTHORIZED_CODE = 401;
	public static final int PARAM_MISS_CODE = 100000;
	public static final int PARAM_TYPE_ERROR_CODE = 100001;
	public static final int PARAM_BIND_ERROR_CODE = 100002;
	public static final int PARAM_VALID_ERROR_CODE = 100003;
	public static final int NOT_FOUND_CODE = 100004;
	public static final int MSG_NOT_READABLE_CODE = 100005;
	public static final int METHOD_NOT_SUPPORTED_CODE = 100006;
	public static final int MEDIA_TYPE_NOT_SUPPORTED_CODE = 100007;
	public static final int MEDIA_TYPE_NOT_ACCEPT_CODE = 100008;
	public static final int REQ_REJECT_CODE = 100009;

	/**
	 * 通用数据层 code
	 */
	public static final int DATA_NOT_EXIST_CODE = 100100;
	public static final int DATA_EXISTED_CODE = 100101;
	public static final int DATA_ADD_FAILED_CODE = 100102;
	public static final int DATA_UPDATE_FAILED_CODE = 100103;
	public static final int DATA_DELETE_FAILED_CODE = 100104;

    /**
     * 商品已经售罄
     */
    public static final int ITEM_SOLD_OUT_CODE = 200001;

	/**
	 * code编码
	 */
	final int code;
	/**
	 * 中文信息描述
	 */
	final String msg;
}
