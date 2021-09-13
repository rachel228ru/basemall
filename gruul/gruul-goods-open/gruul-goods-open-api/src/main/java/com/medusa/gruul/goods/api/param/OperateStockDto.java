package com.medusa.gruul.goods.api.param;

import lombok.*;


/**
 * @Description: 操作商品库存需要的参数
 * @Author: alan
 * @Date: 2019/7/21 10:10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperateStockDto {
    /**
     * skuID
     */
    @EqualsAndHashCode.Include
    private Long skuId;

    /**
     * 商品数量
     */
    private Integer number;
}
