package com.medusa.gruul.goods.service.manager.impl;


import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.goods.service.manager.IFreightTemplateService;
import com.medusa.gruul.logistics.api.feign.RemoteLogisticsFeginService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 运费模版 服务实现类
 * </p>
 *
 * @author alan
 * @since 2019-09-03
 */
@Service
public class FreightTemplateServiceImpl implements IFreightTemplateService {


    @Resource
    private RemoteLogisticsFeginService remoteLogisticsFeginService;

    /**
     * 物流接口获取所有运费模版
     *
     * @return 运费模版list
     */
    @Override
    public Result findFreightTemplateListByLogistics() {
        String shopId = ShopContextHolder.getShopId();
        String tenantId = TenantContextHolder.getTenantId();
        return remoteLogisticsFeginService.getLogisticList(tenantId, shopId);
    }

    /**
     * 物流接口获取单个产品运费模版信息
     *
     * @param freightTemplateId
     * @return 运费模版对象
     */
    @Override
    public Result findFreightTemplateInfoByLogistics(Long freightTemplateId) {
        return remoteLogisticsFeginService.getLogisticInfoById(freightTemplateId);
    }
}
