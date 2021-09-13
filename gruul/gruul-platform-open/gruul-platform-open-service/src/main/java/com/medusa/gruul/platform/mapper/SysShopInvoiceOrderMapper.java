package com.medusa.gruul.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medusa.gruul.platform.api.entity.SysShopInvoiceOrder;

/**
 * <p>
 * 发票工单表 Mapper 接口
 * </p>
 *
 * @author whh
 * @since 2020-08-01
 */
public interface SysShopInvoiceOrderMapper extends BaseMapper<SysShopInvoiceOrder> {

    /**
     * 获取当前最大的id
     *
     * @return com.medusa.gruul.platform.api.entity.SysShopInvoiceOrder
     */
    Integer selectMaxId();

}
