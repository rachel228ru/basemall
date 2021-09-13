package com.medusa.gruul.order.api.constant;

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
public enum  OrderCode  implements IResultCode {

    /**
     * 数据状态异常
     */
    ABNORMAL_DATA_STATUS(OrderCode.ABNORMAL_DATA_STATUS_CODE, "数据状态异常"),
    /**
     * 第三方服务异常
     */
    THIRD_PARTY_SERVICE_EXCEPTION(OrderCode.THIRD_PARTY_SERVICE_EXCEPTION_CODE, "第三方服务异常"),
    /**
     * 数据已经更新
     */
    DATA_HAS_BEEN_UPDATED(OrderCode.DATA_HAS_BEEN_UPDATED_CODE, "数据已经更新,请刷新后重试"),
    /**
     * 不在配送范围内
     */
    NOT_IN_THE_SCOPE_OF_DISTRIBUTION(OrderCode.NOT_IN_THE_SCOPE_OF_DISTRIBUTION_CODE, "不在配送范围内"),
    /**
     * 没有有效的收货地址
     */
    NO_VALID_SHIP_TO_ADDRESS(OrderCode.NO_VALID_SHIP_TO_ADDRESS_CODE, "没有有效的收货地址"),
    /**
     * 账号异常
     */
    ACCOUNT_NUMBER_EXCEPTION (OrderCode.ACCOUNT_NUMBER_EXCEPTION_CODE, "账号异常请联系客服"),
    ;

    public static final int ABNORMAL_DATA_STATUS_CODE = 300001;
    public static final int THIRD_PARTY_SERVICE_EXCEPTION_CODE = 300002;
    public static final int DATA_HAS_BEEN_UPDATED_CODE = 300003;
    public static final int NOT_IN_THE_SCOPE_OF_DISTRIBUTION_CODE = 300004;
    public static final int NO_VALID_SHIP_TO_ADDRESS_CODE = 300005;
    public static final int ACCOUNT_NUMBER_EXCEPTION_CODE = 300006;
    public static final int ASS_FORBIDDEN_CODE = 300007;





    /**
     * code编码
     */
    final int code;
    /**
     * 中文信息描述
     */
    final String msg;

}
