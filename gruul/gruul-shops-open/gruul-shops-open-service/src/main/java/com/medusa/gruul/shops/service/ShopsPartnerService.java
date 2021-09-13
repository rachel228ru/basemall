package com.medusa.gruul.shops.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.shops.api.entity.ShopsPartner;
import com.medusa.gruul.shops.model.vo.ShopsPartnerVo;

/**
 * @author create by zq
 * @date created in 2020/01/14
 */
public interface ShopsPartnerService extends IService<ShopsPartner> {

    /**
     * 获取店铺list
     *
     * @return list
     */
    Result listShopsPartner();


    /**
     * 获取总店 by shopId
     *
     * @param shopId
     * @return shops
     */
    ShopsPartner oneByShopId(String shopId);


    /**
     * 获取总店 by shopId
     *
     * @param tenantId
     * @return shops
     */
    ShopsPartnerVo oneByTenantId(String tenantId);


    /**
     * 新增默认店铺
     *
     * @param tenantId   租户id
     * @param pass       密码
     * @param phone      手机号
     * @param platformId 平台账号id
     * @return Result
     */
    Result<ShopsPartner> saveShopsPartner(String tenantId, String pass, String phone, Long platformId);

    /**
     * 根据平台用户id获取店铺信息
     *
     * @param platformId 平台用户id
     * @param tenantId   租户id
     * @return com.medusa.gruul.shops.api.entity.ShopsPartner
     */
    ShopsPartner getByPlatformIdAndTenantId(Long platformId, String tenantId);
}
