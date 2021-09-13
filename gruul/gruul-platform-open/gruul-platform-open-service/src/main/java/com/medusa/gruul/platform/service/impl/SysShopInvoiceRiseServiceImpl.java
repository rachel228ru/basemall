package com.medusa.gruul.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.CurUserUtil;
import com.medusa.gruul.platform.api.entity.SysShopInvoiceRise;
import com.medusa.gruul.platform.mapper.SysShopInvoiceRiseMapper;
import com.medusa.gruul.platform.model.dto.AddInvoiceDto;
import com.medusa.gruul.platform.model.dto.UpdateInvoiceDto;
import com.medusa.gruul.platform.model.vo.InvoiceListVo;
import com.medusa.gruul.platform.service.ISysShopInvoiceRiseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户发票抬头表 服务实现类
 * </p>
 *
 * @author whh
 * @since 2020-08-01
 */
@Service
public class SysShopInvoiceRiseServiceImpl extends ServiceImpl<SysShopInvoiceRiseMapper, SysShopInvoiceRise> implements ISysShopInvoiceRiseService {

    @Override
    public void addInvoice(AddInvoiceDto balanceRechargeDto) {
        SysShopInvoiceRise sysShopInvoiceRise = new SysShopInvoiceRise();
        BeanUtil.copyProperties(balanceRechargeDto, sysShopInvoiceRise);
        if (ObjectUtil.isNotNull(balanceRechargeDto.getDefaultStatus()) && CommonConstants.NUMBER_ONE.equals(balanceRechargeDto.getDefaultStatus())) {
            SysShopInvoiceRise up = new SysShopInvoiceRise();
            up.setDefaultStatus(CommonConstants.NUMBER_ZERO);
            this.baseMapper.update(up, new QueryWrapper<SysShopInvoiceRise>().eq("default_status", CommonConstants.NUMBER_ONE));
        }
        sysShopInvoiceRise.setAccountId(Long.valueOf(CurUserUtil.getPcRqeustAccountInfo().getUserId()));
        this.baseMapper.insert(sysShopInvoiceRise);
    }

    @Override
    public List<InvoiceListVo> invoiceList(Integer type) {
        List<SysShopInvoiceRise> shopInvoiceRises = this.baseMapper.selectList(
                new QueryWrapper<SysShopInvoiceRise>().eq("account_id", CurUserUtil.getPcRqeustAccountInfo().getUserId())
                        .eq(!type.equals(CommonConstants.NUMBER_ZERO), "head_type", type));
        if (CollectionUtil.isEmpty(shopInvoiceRises)) {
            return null;
        }
        return shopInvoiceRises.stream().map(obj -> BeanUtil.toBean(obj, InvoiceListVo.class)).collect(Collectors.toList());
    }

    @Override
    public void updateInvoice(UpdateInvoiceDto updateInvoiceDto) {
        SysShopInvoiceRise shopInvoiceRise = this.getById(updateInvoiceDto.getId());
        if (ObjectUtil.isNull(shopInvoiceRise)) {
            throw new ServiceException("非法数据");
        }
        Long accountId = Long.valueOf(CurUserUtil.getPcRqeustAccountInfo().getUserId());
        if (!shopInvoiceRise.getAccountId().equals(accountId)) {
            throw new ServiceException("非法操作");
        }
        if (shopInvoiceRise.getDefaultStatus().equals(CommonConstants.NUMBER_ONE)) {
            SysShopInvoiceRise up = new SysShopInvoiceRise();
            up.setDefaultStatus(CommonConstants.NUMBER_ZERO);
            this.baseMapper.update(up, new QueryWrapper<SysShopInvoiceRise>().eq("default_status", CommonConstants.NUMBER_ONE));
        }
        BeanUtil.copyProperties(updateInvoiceDto, shopInvoiceRise);
        this.baseMapper.updateById(shopInvoiceRise);
    }
}
