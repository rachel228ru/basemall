package com.medusa.gruul.shops.service.impl;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.shops.api.entity.Shops;
import com.medusa.gruul.shops.api.entity.ShopsPartner;
import com.medusa.gruul.shops.mapper.ShopsPartnerMapper;
import com.medusa.gruul.shops.model.vo.ShopsPartnerVo;
import com.medusa.gruul.shops.properties.GlobalConstant;
import com.medusa.gruul.shops.service.ShopsPartnerService;
import com.medusa.gruul.shops.service.ShopsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author create by zq
 * @date created in 2020/01/14
 */
@Service(value = "shopsPartnerServiceImpl")
public class ShopsPartnerServiceImpl extends ServiceImpl<ShopsPartnerMapper, ShopsPartner> implements ShopsPartnerService {

    @Autowired
    private ShopsService shopsService;



    /**
     * 获取店铺list
     *
     * @return list
     */
    @Override
    public Result listShopsPartner() {
        return Result.ok(this.baseMapper.selectList(new QueryWrapper<ShopsPartner>()
                .eq("approval_status", GlobalConstant.STRING_ONE)
                .eq("prohibit_status", GlobalConstant.STRING_ZERO)));
    }


    /**
     * 获取店铺 by shopId
     *
     * @param shopId
     * @return shops
     */
    @Override
    public ShopsPartner oneByShopId(String shopId) {
        return baseMapper.selectOne(new QueryWrapper<ShopsPartner>()
                .eq("approval_status", GlobalConstant.STRING_ONE)
                .eq("prohibit_status", GlobalConstant.STRING_ZERO)
                .eq("shop_id", shopId));
    }


    /**
     * 获取店铺 by tenantId
     *
     * @param tenantId
     * @return shops
     */
    @Override
    public ShopsPartnerVo oneByTenantId(String tenantId) {
        return baseMapper.oneByTenantId(tenantId);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<ShopsPartner> saveShopsPartner(String tenantId, String pass, String phone, Long platformId) {
        //查询默认账号是否存在存在;存在则不做操作直接返回
        ShopsPartner shopsPartner = this.baseMapper.selectByTenantIdAndPartnerIdIsNull(tenantId);
        if (ObjectUtil.isNotNull(shopsPartner)) {
            return Result.ok(shopsPartner);
        }
        TenantContextHolder.setTenantId(tenantId);
        shopsPartner = new ShopsPartner();
        shopsPartner.setPass(pass);
        shopsPartner.setPhone(phone);
        shopsPartner.setInvitationCode(generateInvitationCode());
        shopsPartner.setPlatformId(platformId);
        shopsPartner.setProhibitStatus(GlobalConstant.STRING_ZERO);
        shopsPartner.setApprovalStatus(GlobalConstant.STRING_ONE);
        log.warn("当前tenantId".concat(tenantId));
        String shopId = tenantId.concat(String.valueOf(100000 + shopsService.getMaxId()));
        ShopContextHolder.setShopId(shopId);
        int insert = this.baseMapper.insert(shopsPartner);
        if (insert < 1 || null == shopsPartner.getId()) {
            throw new ServiceException(String.format("insert [shopPartner] fail! status is not 1. tenantId : %s, pass : %s, phone : %s", tenantId, pass, phone));
        }
        Shops shops = new Shops();
        if (!shopsService.save(shops)) {
            throw new ServiceException(String.format("insert [shop] fail! status is not 1. tenantId : %s, shopId : %s", tenantId, shopsPartner.getId()));
        }
        //再添加之后在进行set不然mybatis plus insert时会出错
        shopsPartner.setShopId(shopId);
        return Result.ok(shopsPartner);
    }

    @Override
    public ShopsPartner getByPlatformIdAndTenantId(Long platformId, String tenantId) {
        return this.baseMapper.selectByPlatformIdAndTenantId(platformId, tenantId);
    }

    private String generateInvitationCode() {
        int randomInt = RandomUtil.randomInt(10000, 99999);
        ShopsPartner shopsPartner = this.baseMapper.selectOne(new QueryWrapper<ShopsPartner>().eq("invitation_code", randomInt));
        if (shopsPartner != null) {
            return generateInvitationCode();
        }
        return String.valueOf(randomInt);
    }
}
