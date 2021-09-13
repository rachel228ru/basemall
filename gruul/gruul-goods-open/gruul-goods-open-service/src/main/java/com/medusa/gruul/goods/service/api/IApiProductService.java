package com.medusa.gruul.goods.service.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.goods.api.entity.Product;
import com.medusa.gruul.goods.api.model.param.api.ApiProductParam;
import com.medusa.gruul.goods.api.model.vo.api.ApiAliveProductVo;
import com.medusa.gruul.goods.api.model.vo.api.ApiProductVo;
import com.medusa.gruul.goods.api.model.vo.api.ApiShowCategoryProductVo;

import java.util.List;

/**
 * 小程序商品信息 服务类
 *
 * @author kyl
 * @since 2019-10-06
 */
public interface IApiProductService extends IService<Product> {

    /**
     * 根据主键id查询商品详情
     *
     * @param id 商品id
     * @return 商品信息
     */
    ApiProductVo getProductById(Long id);

    /**
     * 商品分页列表
     *
     * @param productParam 商品查询条件
     * @return 分页对象
     */
    IPage<ApiAliveProductVo> getPageList(ApiProductParam productParam);

    /**
     * 商超系统分类列表
     *
     * @param productParam 商品查询条件
     * @return 分页对象
     */
    IPage<ApiAliveProductVo> getSupermarketList(ApiProductParam productParam);
    //=============================================商品组件根据商品集合匹配未删除的商品===================================================

    /**
     * 根据商品数组匹配未删除的商品
     *
     * @param ids
     * @param launchArea
     * @param saleMode
     * @return Result<List < ApiAliveProductVo>>
     */
    List<ApiAliveProductVo> getAliveProductList(Long[] ids, String launchArea, Long saleMode);

    //=============================================商品分类页组件根据商品分类集合匹配对应分类下的商品===================================================

    /**
     * 商品分类集合匹配对应分类下的商品
     *
     * @param ids
     * @param saleMode
     * @return List<ApiShowCategoryProductVo>
     */
    List<ApiShowCategoryProductVo> getAliveProductListByCategory(Long[] ids, Long saleMode);

    /**
     * pc商品分类集合匹配对应分类下的商品
     *
     * @param ids
     * @return List<ApiShowCategoryProductVo>
     */
    List<ApiShowCategoryProductVo> getAliveProductListGroupByCategory(Long[] ids);


}
