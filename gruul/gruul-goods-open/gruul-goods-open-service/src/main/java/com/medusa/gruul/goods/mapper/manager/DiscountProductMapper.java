package com.medusa.gruul.goods.mapper.manager;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.medusa.gruul.goods.api.entity.Product;
import com.medusa.gruul.goods.api.model.param.manager.DiscountProductParam;
import com.medusa.gruul.goods.api.model.vo.manager.DiscountProductVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 商品信息 Mapper 接口
 * </p>
 *
 * @author lcysike
 * @since 2019/12/18
 */
@Repository
public interface DiscountProductMapper extends BaseMapper<Product> {
    /**
     * 已发货的订单商品移除列表
     *
     * @param page
     * @param discountProductParam
     * @return 商品list对象
     */
    List<DiscountProductVo> queryRemoveProductList(IPage page, @Param("discountProductParam") DiscountProductParam discountProductParam);

    /**
     * 根据商品数组匹配未删除的商品
     *
     * @param ids
     * @return List<DiscountProductVo>
     */
    List<DiscountProductVo> querySaveProductList(@Param("ids") List<Long> ids);

    /**
     * 批量查询多个商品详情
     *
     * @param productIds
     * @return List<DiscountProductVo>
     */
    List<DiscountProductVo> queryDiscountProductTypeList(@Param("productIds") List<Long> productIds);
}
