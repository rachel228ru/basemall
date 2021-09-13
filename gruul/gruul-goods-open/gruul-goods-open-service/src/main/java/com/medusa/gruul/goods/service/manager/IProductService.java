package com.medusa.gruul.goods.service.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.common.core.util.ResultList;
import com.medusa.gruul.goods.api.entity.Product;
import com.medusa.gruul.goods.api.model.dto.manager.ProductDto;
import com.medusa.gruul.goods.api.model.dto.manager.ProductShowCategoryDto;
import com.medusa.gruul.goods.api.model.dto.manager.SkuStockDto;
import com.medusa.gruul.goods.api.model.param.manager.DiscountProductParam;
import com.medusa.gruul.goods.api.model.param.manager.ProductParam;
import com.medusa.gruul.goods.api.model.vo.manager.DiscountProductVo;
import com.medusa.gruul.goods.api.model.vo.manager.ItemVo;
import com.medusa.gruul.goods.api.model.vo.manager.ProductVo;
import com.medusa.gruul.goods.api.model.vo.manager.SkuStockVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 商品信息 服务类
 * </p>
 *
 * @author alan
 * @since 2019-09-03
 */
public interface IProductService extends IService<Product> {

    /**
     * 查询列表
     *
     * @param productParam
     * @return 分页对象
     */
    IPage<ProductVo> getProductList(ProductParam productParam);

    /**
     * 查询供应商商品列表
     *
     * @param productParam
     * @return 分页对象
     */
    IPage<ProductVo> getSupplierProductList(ProductParam productParam);

    /**
     * 查询单个商品信息
     *
     * @param id
     * @return ProductVo对象
     */
    ProductVo getProductById(Long id);

    /**
     * 根据产品id获取sku与会员价信息
     *
     * @param id 商品id
     * @return sku对象list
     */
    List<SkuStockVo> getSkuStockAndMemberPriceById(Long id);

    /**
     * 产品发布
     *
     * @param productDto
     */
    void issueProduct(ProductDto productDto);


    /**
     * 产品修改
     *
     * @param productDto
     */
    void updateProduct(ProductDto productDto);

    /**
     * 产品展示分类设置
     *
     * @param productShowCategoryDtos
     * @param productIds
     */
    void updateProductShowCategory(List<ProductShowCategoryDto> productShowCategoryDtos, Long[] productIds);

    /**
     * 产品专区变更
     *
     * @param ids
     * @param saleMode
     */
    void updateProductSaleMode(Long[] ids, Long saleMode);



    /**
     * 产品上下架
     *
     * @param ids
     * @param status
     */
    void updateProductStatus(Long[] ids, Integer status);


    /**
     * 产品删除
     *
     * @param ids
     */
    void deleteProductList(Long[] ids);

    /**
     * 获取单个sku关联商品会员价信息
     *
     * @param skuIds
     * @return ItemVo
     */
    List<ItemVo> findItemVoByIds(List<Long> skuIds);




    /**
     * 已发货的订单商品移除列表
     *
     * @param discountProductParam
     * @return 分页对象
     */
    IPage<DiscountProductVo> getRemoveProductList(DiscountProductParam discountProductParam);

    /**
     * 根据商品数组匹配未删除的商品
     *
     * @param ids
     * @return Result<List < DiscountProductVo>>
     */
    List<DiscountProductVo> getAliveProductList(Long[] ids);

    /**
     * 批量查询多个商品详情
     *
     * @param productIds
     * @return List<DiscountProductVo>
     */
    List<DiscountProductVo> getDiscountProductTypeList(List<Long> productIds);

    /**
     * 查询上架商品的数量
     *
     * @return Integer
     */
    Integer getProductCount();


    /**
     * 查询运费模版id是否被商品使用
     *
     * @param templateId
     * @return Boolean
     */
    Boolean checkProductByTemplateId(Long templateId);



    /**
     * 新建店铺同时新增默认店铺里面的默认商品
     *
     * @return Result
     */
    void createDefaultProduct();

    /**
     * 淘宝商品csv文件导入
     *
     * @param file 商品csv文件
     * @return Result
     */
    Result importCsvProduct(MultipartFile file);

    /**
     * 查询素材库商品列表
     *
     * @param productParam
     * @return 分页对象
     */
    IPage<ProductVo> getCsvProductList(ProductParam productParam);


}
