package com.medusa.gruul.goods.service.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.goods.api.entity.SkuStock;
import com.medusa.gruul.goods.api.param.OperateStockDto;

import java.util.List;

/**
 * <p>
 * sku的库存 服务类
 * </p>
 *
 * @author alan
 * @since 2019-09-03
 */
public interface ISkuStockService extends IService<SkuStock> {

    /**
     * 减少库存
     *
     * @param operateStockDto
     * @return Boolean
     * @Author alan
     * @Date 2019/9/9 20:34
     */
    Boolean subtractStock(OperateStockDto operateStockDto);

    /**
     * 批量减少库存
     *
     * @param operateStockDtoList
     * @return Boolean
     * @Author alan
     * @Date 2019/9/9 20:34
     */
    Boolean batchSubtractStock(List<OperateStockDto> operateStockDtoList);

    /**
     * 批量归还库存
     *
     * @param operateStockDtoList
     * @return java.lang.Boolean
     * @author alan
     * @date 2019/11/28 21:23
     */
    Boolean batchRevertStock(List<OperateStockDto> operateStockDtoList);
}
