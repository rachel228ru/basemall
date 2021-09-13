package com.medusa.gruul.goods.mq;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.goods.api.constant.GoodsMyQueneName;
import com.medusa.gruul.goods.service.manager.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 商品消息监听
 * </p>
 *
 * @author lcysike
 * @since 2019-11-19
 */

@Slf4j
@Service
public class GoodsListener {

    @Resource
    private IProductService productService;

    @RabbitListener(queues = GoodsMyQueneName.GENERATE_DEFAULT_GOODS)
    public void receive(String message) {
        log.info("收到超时自动完成消息:deliveryTag{},当前时间{},消息内容{}.", DateUtil.now(), message);
        try {
            Map map = JSON.parseObject(message);
            String tenantId = String.valueOf(map.get("tenantId"));
            String shopId = String.valueOf(map.get("shopId"));
            TenantContextHolder.setTenantId(tenantId);
            ShopContextHolder.setShopId(shopId);
            //生成默认商品关联数据信息
            productService.createDefaultProduct();
            log.info("超时自动完成执行完成: deliveryTag{}");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
