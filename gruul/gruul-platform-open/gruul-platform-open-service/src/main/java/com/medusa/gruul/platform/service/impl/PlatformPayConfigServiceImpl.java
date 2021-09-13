package com.medusa.gruul.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.platform.api.entity.PlatformPayConfig;
import com.medusa.gruul.platform.api.entity.PlatformShopInfo;
import com.medusa.gruul.platform.api.model.vo.PayInfoVo;
import com.medusa.gruul.platform.mapper.PlatformPayConfigMapper;
import com.medusa.gruul.platform.service.IPlatformShopInfoService;
import com.medusa.gruul.platform.service.PlatformPayConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author whh
 */
@Service
public class PlatformPayConfigServiceImpl extends ServiceImpl<PlatformPayConfigMapper, PlatformPayConfig> implements PlatformPayConfigService {


    @Autowired
    private IPlatformShopInfoService platformShopInfoService;

    @Override
    public PlatformPayConfig getByTenantId(String tenantId) {
        return this.getBaseMapper().selectOne(new QueryWrapper<PlatformPayConfig>().eq("tenant_id", tenantId));
    }

    @Override
    public void init(String tenantId) {
        PlatformPayConfig platformPayConfig = new PlatformPayConfig();
        platformPayConfig.setTenantId(tenantId);
        this.getBaseMapper().insert(platformPayConfig);
    }

}
