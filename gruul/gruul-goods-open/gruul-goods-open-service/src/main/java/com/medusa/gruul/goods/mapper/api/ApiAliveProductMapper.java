package com.medusa.gruul.goods.mapper.api;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.medusa.gruul.goods.api.entity.Product;
import com.medusa.gruul.goods.api.model.param.api.ApiProductParam;
import com.medusa.gruul.goods.api.model.vo.api.ApiAliveProductVo;
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
public interface ApiAliveProductMapper extends BaseMapper<Product> {

    /**
     * 根据商品数组匹配未删除的商品
     *
     * @param ids
     * @param saleMode
     * @return List<ApiAliveProductVo>
     */
    List<ApiAliveProductVo> querySaveProductList(@Param("ids") List<Long> ids, @Param("saleMode")Long saleMode);

    /**
     * 获取社区商超商品信息list
     *
     * @param page
     * @param apiProductParam 商品信息查询参数
     * @return 商品list信息
     */
    List<ApiAliveProductVo> querySuperMarketProductList(IPage page, @Param("apiProductParam") ApiProductParam apiProductParam);


    /**
     * 获取分类下的商品list
     *
     * @param secondShowCategoryId
     * @return 商品list信息
     */
    List<ApiAliveProductVo> queryShowCategoryProductList(@Param("secondShowCategoryId") Long secondShowCategoryId);

    /**
     * 获取分类下的商品list 带专区分类
     *
     * @param secondShowCategoryId
     * @param saleMode
     * @return 商品list信息
     */
    List<ApiAliveProductVo> queryShowCategoryProductListBySaleMode(@Param("secondShowCategoryId") Long secondShowCategoryId, @Param("saleMode") Long saleMode);
}
