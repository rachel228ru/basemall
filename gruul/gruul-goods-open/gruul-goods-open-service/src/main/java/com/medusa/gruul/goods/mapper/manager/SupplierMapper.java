package com.medusa.gruul.goods.mapper.manager;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.medusa.gruul.goods.api.entity.Supplier;
import com.medusa.gruul.goods.api.model.param.manager.SupplierParam;
import com.medusa.gruul.goods.api.model.vo.manager.SupplierVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 供应商 Mapper接口
 *
 * @author kyl
 * @since 2019-10-06
 */
@Repository
public interface SupplierMapper extends BaseMapper<Supplier> {

    /**
     * 获取所有供应商
     *
     * @return 所有供应商
     */
    List<SupplierVo> queryAllSupplierList();

    /**
     * 获取所有供应商条数 供应商识别号生成使用
     *
     * @return 供应商条数
     */
    @SqlParser(filter = true)
    Integer queryAllCount();

    /**
     * 获取供应商详情
     *
     * @param id 供应商id
     * @return 供应商信息
     */
    SupplierVo queryByPrimaryKey(Long id);

    /**
     * 获取供应商列表
     *
     * @param page
     * @param supplierParam
     * @return 供应商list信息
     */
    List<SupplierVo> querySupplierList(IPage page, @Param("supplierParam") SupplierParam supplierParam);

    /**
     * 根据供应商id数组查询供应商信息
     *
     * @param supplierIds
     * @return 供应商list信息
     */
    List<SupplierVo> queryDataSetSupplierList(@Param("supplierIds") List<Long> supplierIds);
}
