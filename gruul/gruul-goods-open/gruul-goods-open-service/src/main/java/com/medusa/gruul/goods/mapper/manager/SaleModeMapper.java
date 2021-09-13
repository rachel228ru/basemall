package com.medusa.gruul.goods.mapper.manager;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.medusa.gruul.goods.api.entity.SaleMode;
import com.medusa.gruul.goods.api.model.param.manager.SaleModeParam;
import com.medusa.gruul.goods.api.model.vo.manager.SaleModeVo;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;


import java.util.List;

/**
 * <p>
 * 商品自定义专区 Mapper 接口
 * </p>
 *
 * @author alan
 * @since 2019-09-03
 */
@Repository
public interface SaleModeMapper extends BaseMapper<SaleMode> {


    /**
     * 获取所有专区信息
     *
     * @param page 分页对象
     * @param saleModeParam 销售专区查询参数
     * @return 所有专区list信息
     */
    List<SaleModeVo> querySaleModeList(IPage page, @Param("saleModeParam") SaleModeParam saleModeParam);

    /**
     * 获取所有专区信息
     *
     * @return 所有专区list信息
     */
    List<SaleModeVo> queryAllSaleModeList();

    /**
     * 获取商品专区最大排序值
     *
     * @return Integer
     */
    Integer queryMaxSort();

    /**
     * 获取默认商品专区信息
     *
     * @return 所有默认商品专区list信息
     */
    List<SaleModeVo> queryDefaultSaleMode();

}
