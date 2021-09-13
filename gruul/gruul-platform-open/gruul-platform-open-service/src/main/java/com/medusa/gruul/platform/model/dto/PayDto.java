package com.medusa.gruul.platform.model.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author whh
 * @description
 * @data: 2020/8/1
 */
@Data
public class PayDto {

    /**
     * 预支付回话标识
     */
    private String prepayId;

    /**
     * trade_type为NATIVE时有返回，用于生成二维码，展示给用户进行扫码支付
     */
    private String codeUrl;
}
