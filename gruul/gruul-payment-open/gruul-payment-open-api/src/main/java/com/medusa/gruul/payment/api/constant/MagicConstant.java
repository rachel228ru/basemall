package com.medusa.gruul.payment.api.constant;

import java.math.BigDecimal;

/**
 * @author whh
 * @date 2019/11/06
 */
public class MagicConstant {

    /**
     * 最小金额值
     */
    public static final BigDecimal MIN_MONEY = BigDecimal.valueOf(0.01);

    public static final String LOCAL_IP = "127.0.0.1";

    public static final String TIMEOUT_EXPRESS_REGEX = "^\\d{1,}(m|h|d)$";
}
