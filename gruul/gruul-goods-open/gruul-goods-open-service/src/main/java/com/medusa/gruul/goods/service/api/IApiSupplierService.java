package com.medusa.gruul.goods.service.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.goods.api.entity.Supplier;
import com.medusa.gruul.goods.api.model.dto.api.ApiSupplierDto;

/**
 * 小程序供应商 服务类
 *
 * @author: KYL
 * @since: 2019/11/13
 */
public interface IApiSupplierService extends IService<Supplier> {

    /**
     * 添加供应商
     *
     * @param supplierDto
     */
    void addSupplier(ApiSupplierDto supplierDto);

}
