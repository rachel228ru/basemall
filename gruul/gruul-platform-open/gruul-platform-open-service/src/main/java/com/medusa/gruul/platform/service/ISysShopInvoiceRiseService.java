package com.medusa.gruul.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.platform.api.entity.SysShopInvoiceRise;
import com.medusa.gruul.platform.model.dto.AddInvoiceDto;
import com.medusa.gruul.platform.model.dto.UpdateInvoiceDto;
import com.medusa.gruul.platform.model.vo.InvoiceListVo;

import java.util.List;

/**
 * <p>
 * 用户发票抬头表 服务类
 * </p>
 *
 * @author whh
 * @since 2020-08-01
 */
public interface ISysShopInvoiceRiseService extends IService<SysShopInvoiceRise> {

    /**
     * 新增用户发票抬头
     *
     * @param balanceRechargeDto com.medusa.gruul.platform.model.dto.AddInvoiceDto
     */
    void addInvoice(AddInvoiceDto balanceRechargeDto);

    /**
     * 查询用户自身用户发票抬头
     *
     * @param type
     * @return com.medusa.gruul.platform.model.vo.InvoiceListVo
     */
    List<InvoiceListVo> invoiceList(Integer type);

    /**
     * 更新用户发票抬头
     *
     * @param updateInvoiceDto com.medusa.gruul.platform.model.dto.UpdateInvoiceDto
     */
    void updateInvoice(UpdateInvoiceDto updateInvoiceDto);

}
