package com.medusa.gruul.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.platform.api.entity.PlatformPayConfig;
import com.medusa.gruul.platform.api.model.vo.PayInfoVo;

/**
 * @author whh
 */
public interface PlatformPayConfigService extends IService<PlatformPayConfig> {


    /**
     * 获取指定租户的支付配置
     *
     * @param tenantId
     * @return com.medusa.gruul.platform.api.entity.PlatformPayConfig
     */
    PlatformPayConfig getByTenantId(String tenantId);

    /**
     * 生成默认支付配置
     *
     * @param tenantId 租户id
     */
    void init(String tenantId);


}
