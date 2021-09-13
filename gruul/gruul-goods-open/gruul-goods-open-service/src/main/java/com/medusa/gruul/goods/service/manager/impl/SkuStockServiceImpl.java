package com.medusa.gruul.goods.service.manager.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.goods.api.constant.GoodsSkuStockRedisKey;
import com.medusa.gruul.goods.api.entity.SkuStock;
import com.medusa.gruul.goods.api.param.OperateStockDto;
import com.medusa.gruul.goods.mapper.manager.SkuStockMapper;
import com.medusa.gruul.goods.service.manager.ISkuStockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * sku的库存 服务实现类
 * </p>
 *
 * @author alan
 * @since 2019-09-03
 */
@Service
public class SkuStockServiceImpl extends ServiceImpl<SkuStockMapper, SkuStock> implements ISkuStockService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean subtractStock(OperateStockDto operateStockDto) {
        GoodsSkuStockRedisKey goodsSkuStockRedisKey = new GoodsSkuStockRedisKey();
        int numAttempts = 0;
        do {
            SkuStock skuStock = baseMapper.selectById(operateStockDto.getSkuId());
            if (skuStock.getStock() >= operateStockDto.getNumber()) {
                skuStock.setStock(skuStock.getStock() - operateStockDto.getNumber());
                skuStock.setSale(skuStock.getSale() + operateStockDto.getNumber());
                if (this.updateById(skuStock)) {
                    goodsSkuStockRedisKey.set(String.valueOf(skuStock.getId()), String.valueOf(skuStock.getStock()));
                    return true;
                } else {
                    numAttempts++;
                }
            }
        } while (numAttempts < CommonConstants.DEFAULT_MAX_RETRIES);
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchSubtractStock(List<OperateStockDto> operateStockDtoList) {
        GoodsSkuStockRedisKey goodsSkuStockRedisKey = new GoodsSkuStockRedisKey();
        List<Long> skuIdList =
                operateStockDtoList.stream().map(OperateStockDto::getSkuId).collect(Collectors.toList());
        List<SkuStock> skuStockList =
                baseMapper.selectBatchIds(skuIdList);
        Map<Long, SkuStock> skuStockMap = skuStockList.stream().collect(Collectors.toMap(SkuStock::getId, v -> v));
        skuStockList.clear();
        boolean re = false;
        for (OperateStockDto operateStockDto : operateStockDtoList) {
            SkuStock skuStock = skuStockMap.get(operateStockDto.getSkuId());
            if (skuStock.getStock() >= operateStockDto.getNumber()) {
                skuStock.setStock(skuStock.getStock() - operateStockDto.getNumber());
                skuStock.setSale(skuStock.getSale() + operateStockDto.getNumber());
                //更新数据库库存与销量 同时更新缓存里面的库存
                if (this.updateById(skuStock)) {
                    goodsSkuStockRedisKey.set(String.valueOf(skuStock.getId()), String.valueOf(skuStock.getStock()));
                    re = true;
                }
            }
        }
        return re;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchRevertStock(List<OperateStockDto> operateStockDtoList) {
        GoodsSkuStockRedisKey goodsSkuStockRedisKey = new GoodsSkuStockRedisKey();
        List<Long> skuIdList =
                operateStockDtoList.stream().map(OperateStockDto::getSkuId).collect(Collectors.toList());
        List<SkuStock> skuStockList =
                baseMapper.selectBatchIds(skuIdList);
        Map<Long, SkuStock> skuStockMap = skuStockList.stream().collect(Collectors.toMap(SkuStock::getId, v -> v));
        skuStockList.clear();
        for (OperateStockDto operateStockDto : operateStockDtoList) {
            SkuStock skuStock = skuStockMap.get(operateStockDto.getSkuId());
            if (!BeanUtil.isEmpty(skuStock)) {
                skuStock.setStock(skuStock.getStock() + operateStockDto.getNumber());
                skuStock.setSale(skuStock.getSale() - operateStockDto.getNumber());
                //更新数据库库存与销量 同时更新缓存里面的库存
                if (this.updateById(skuStock)) {
                    goodsSkuStockRedisKey.set(String.valueOf(skuStock.getId()), String.valueOf(skuStock.getStock()));
                    return true;
                }
                goodsSkuStockRedisKey.set(String.valueOf(skuStock.getId()), String.valueOf(skuStock.getStock()));
            }
        }
        return false;
    }
}
