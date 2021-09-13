package com.medusa.gruul.goods.service.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.goods.api.entity.Supplier;
import com.medusa.gruul.goods.api.model.dto.manager.SupplierDto;
import com.medusa.gruul.goods.api.model.param.manager.SupplierParam;
import com.medusa.gruul.goods.api.model.vo.manager.SupplierVo;

import java.util.List;

/**
 * 供应商 服务类
 *
 * @author kyl
 * @since 2019-10-06
 */
public interface ISupplierService extends IService<Supplier> {

    /**
     * 获取所有供应商
     *
     * @return
     */
    List<SupplierVo> getAllSupplierList();

    /**
     * 供应商分页列表
     *
     * @param supplierParam
     * @return
     */
    IPage<SupplierVo> getSupplierList(SupplierParam supplierParam);

    /**
     * 添加供应商
     *
     * @param supplierDto
     */
    void addSupplier(SupplierDto supplierDto);

    /**
     * 修改供应商
     *
     * @param supplierDto
     */
    void updateSupplier(SupplierDto supplierDto);

    /**
     * 审核供应商
     *
     * @param supplierDto
     */
    void checkSupplier(SupplierDto supplierDto);

    /**
     * 删除供应商
     *
     * @param ids
     */
    void deleteSupplierList(Long[] ids);

    /**
     * 根据供应商id数组查询供应商信息
     *
     * @param supplierIds
     * @return List<SupplierVo>
     */
    List<SupplierVo> getDataSetSupplierList(List<Long> supplierIds);
}
