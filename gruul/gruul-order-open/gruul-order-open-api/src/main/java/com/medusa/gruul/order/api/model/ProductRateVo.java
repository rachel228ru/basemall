package com.medusa.gruul.order.api.model;

import lombok.Data;

/**
 * ProductRateVo.java
 *
 * @author alan
 * @date 2020/2/7 20:22
 */
@Data
public class ProductRateVo {
    /**
     * 分数
     */
    private Double rate;
    /**
     * 商品ID
     */
    private Long productId;
}
