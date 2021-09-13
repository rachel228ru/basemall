package com.medusa.gruul.goods.task;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.goods.api.constant.GoodsSkuStockRedisKey;
import com.medusa.gruul.goods.api.constant.ShoppingCartRedisKey;
import com.medusa.gruul.goods.api.entity.SkuStock;
import com.medusa.gruul.goods.api.model.vo.api.ApiShoppingCartVo;
import com.medusa.gruul.goods.service.manager.ISkuStockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @description: UpdateSkuTask.java
 * @author: alan
 * @date: 2019/11/22 21:41
 */
@Slf4j
@Component
public class UpdateSkuTask {

    @Resource
    private ISkuStockService skuStockService;

    /**
     * sku库存定时任务
     */
    @Scheduled(fixedRate = 1000 * 60 * 10)
    public void updateSkuTask() {
        log.info("-----------sku库存定时任务开启-----------");
        GoodsSkuStockRedisKey skuStockRedisKey = new GoodsSkuStockRedisKey();
        for (SkuStock skuStock : skuStockService.list()) {
            skuStockRedisKey.set(skuStock.getId().toString(), skuStock.getStock().toString());
        }
        log.info("-----------sku库存定时任务结束-----------");
    }

    /**
     * 购物车选中状态每晚12点定时更新为未选中
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateShoppingCartSelectStatusTask() {
        ShoppingCartRedisKey shoppingCartRedisKey = new ShoppingCartRedisKey();
        log.info("-----------购物车选中状态定时任务开启-----------");
        //每晚12点更新购物车选中状态
        Set<String> keys = shoppingCartRedisKey.keys("*");
        if(CollectionUtil.isNotEmpty(keys)){
            keys.stream().forEach(key -> {
                List<String> stringList = shoppingCartRedisKey.hvals(key);
                if (CollectionUtil.isNotEmpty(stringList)) {
                    List<ApiShoppingCartVo> apiShoppingCartVos = JSON.parseArray(String.valueOf(stringList), ApiShoppingCartVo.class);
                    if (CollectionUtil.isNotEmpty(apiShoppingCartVos)) {
                        apiShoppingCartVos.stream().forEach(apiShoppingCartVo -> {
                            if(CommonConstants.NUMBER_ONE.equals(apiShoppingCartVo.getSelectStatus())){
                                apiShoppingCartVo.setSelectStatus(CommonConstants.NUMBER_ZERO);
                                shoppingCartRedisKey.hset(key, JSON.toJSONString(apiShoppingCartVo.getSkuId()), JSON.toJSONString(apiShoppingCartVo));
                            }
                        });
                    }
                }
            });
        }
        log.info("-----------购物车选中状态定时任务结束-----------");
    }

}
