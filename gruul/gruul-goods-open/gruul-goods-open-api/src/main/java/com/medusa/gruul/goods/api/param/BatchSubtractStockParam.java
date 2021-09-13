package com.medusa.gruul.goods.api.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @Description: 批量减少商品库存的参数
 * @Author: alan
 * @Date: 2019/9/7 13:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchSubtractStockParam {
    private Set<OperateStockDto> skuSet;
}
