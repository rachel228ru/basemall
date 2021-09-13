package com.medusa.gruul.shops.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medusa.gruul.shops.api.entity.ShopsPartner;
import com.medusa.gruul.shops.model.vo.ShopsPartnerVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author create by zq
 * @date created in 2019/11/15
 */
@Repository
public interface ShopsPartnerMapper extends BaseMapper<ShopsPartner> {

    /**
     * 获取店铺 by tenantId
     *
     * @param tenantId
     * @return shops
     */
    @InterceptorIgnore(tenantLine = "true")
    ShopsPartnerVo oneByTenantId(String tenantId);

    /**
     * the is selectByPlatformIdAndTenantId
     * @param platformId 平台id
     * @param tenantId 租户id
     * @return ShopsPartner
     */
    @SqlParser(filter = true)
    ShopsPartner selectByPlatformIdAndTenantId(@Param("platformId") Long platformId, @Param("tenantId") String tenantId);

    /**
     * the is selectByTenantIdAndPartnerIdIsNull
     *
     * @param tenantId 租户id
     * @return ShopsPartner
     */
    @SqlParser(filter = true)
    ShopsPartner selectByTenantIdAndPartnerIdIsNull(@Param("tenantId") String tenantId);

}
