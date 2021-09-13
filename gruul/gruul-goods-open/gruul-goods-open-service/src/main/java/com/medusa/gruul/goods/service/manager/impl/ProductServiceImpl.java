package com.medusa.gruul.goods.service.manager.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.*;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.common.dto.CurShopInfoDto;
import com.medusa.gruul.goods.api.constant.GoodsConstant;
import com.medusa.gruul.goods.api.constant.GoodsProductRedisKey;
import com.medusa.gruul.goods.api.constant.GoodsSkuStockRedisKey;
import com.medusa.gruul.goods.api.entity.*;
import com.medusa.gruul.goods.api.model.dto.manager.*;
import com.medusa.gruul.goods.api.model.param.manager.DiscountProductParam;
import com.medusa.gruul.goods.api.model.param.manager.ProductParam;
import com.medusa.gruul.goods.api.model.vo.api.ApiShoppingCartProductVo;
import com.medusa.gruul.goods.api.model.vo.manager.*;
import com.medusa.gruul.goods.mapper.manager.*;
import com.medusa.gruul.goods.service.manager.ICommanderService;
import com.medusa.gruul.goods.service.manager.IProductService;
import com.medusa.gruul.goods.util.CsvFileUtil;
import com.medusa.gruul.goods.web.enums.ProductStatusEnum;
import com.medusa.gruul.goods.web.enums.SaleModeEnum;
import com.medusa.gruul.order.api.feign.RemoteOrderService;
import com.medusa.gruul.order.api.model.ProductRateVo;
import com.medusa.gruul.oss.api.feign.RemoteSysOssService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * <p>
 * 商品信息 服务实现类
 * </p>
 *
 * @author alan
 * @since 2019-09-03
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CsvProductMapper csvProductMapper;

    @Autowired
    private ProductAttributeMapper productAttributeMapper;

    @Autowired
    private AttributeTemplateMapper attributeTemplateMapper;


    @Autowired
    private SaleModeMapper saleModeMapper;

    @Autowired
    private ShowCategoryMapper showCategoryMapper;

    @Autowired
    private SkuStockMapper skuStockMapper;

    @Autowired
    private ProductShowCategoryMapper productShowCategoryMapper;

    @Autowired
    private DiscountProductMapper discountProductMapper;


    @Resource
    private RemoteOrderService remoteOrderService;


    @Resource
    private RemoteSysOssService remoteSysOssService;

    @Resource
    private ICommanderService commanderService;

    /**
     * 获取商品分页信息
     *
     * @param productParam
     * @return 商品分页对象
     */
    @Override
    public IPage<ProductVo> getProductList(ProductParam productParam) {
        IPage<ProductVo> page = new Page<>(productParam.getCurrent(), productParam.getSize());
        List<ProductVo> productVos = this.baseMapper.queryProductList(page, productParam);
        return page.setRecords(productVos);
    }

    /**
     * 获取供应商商品分页信息
     *
     * @param productParam
     * @return 商品分页对象
     */
    @Override
    public IPage<ProductVo> getSupplierProductList(ProductParam productParam) {
        IPage<ProductVo> page = new Page<>(productParam.getCurrent(), productParam.getSize());
        List<ProductVo> productVos = this.baseMapper.querySupplierProductList(page, productParam);
        return page.setRecords(productVos);
    }



    /**
     * 设置商品评分字段公共方法
     *
     * @param productVo
     * @param productRateVos
     */
    public void setProductRate(ProductVo productVo, List<ProductRateVo> productRateVos) {
        if (CollectionUtil.isNotEmpty(productRateVos)) {
            productRateVos.stream().forEach(productRateVo -> {
                if (productVo.getId().equals(productRateVo.getProductId())) {
                    if (productRateVo.getRate() != null) {
                        productVo.setScore(BigDecimal.valueOf(productRateVo.getRate()));
                    }
                }
            });
        }
    }



    /**
     * 获取单个商品基础信息
     *
     * @param id
     * @return 商品基础信息对象
     */
    @Override
    public ProductVo getProductById(Long id) {
        ProductVo productVo = this.baseMapper.getProductById(id);
        if (BeanUtil.isEmpty(productVo)) {
            throw new ServiceException("商品不存在！", SystemCode.DATA_EXISTED.getCode());
        }
        //获取当前线程中的租户id的店铺信息;getTemplateCodeEnum 获取当前店铺使用模板类型
        CurShopInfoDto tenantIdShopInfo = CurUserUtil.getTenantIdShopInfo();
        return productVo;
    }



    /**
     * 根据商品id获取商品sku与会员价信息
     *
     * @param id
     * @return 商品sku list信息
     */
    @Override
    public List<SkuStockVo> getSkuStockAndMemberPriceById(Long id) {
        return productMapper.querySkuStock(id);
    }

    /**
     * 商品信息的发布 新增商品信息同时插入运费模版、sku、商品属性、展示分类信息，同时清除缓存数据
     *
     * @param productDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void issueProduct(ProductDto productDto) {
        //调用生成货号方法生成商品货号
        String productSn = getProductSn();
        productDto.setProductSn(productSn);
        //状态默认上架
        if (productDto.getStatus() == null) {
            productDto.setStatus(ProductStatusEnum.SELL_ON.getStatus());
        }
        Product product = productDto.coverProduct();
        //评分默认5.0
        if (product.getScore() == null) {
            product.setScore(BigDecimal.valueOf(5.0));
        }
        //商品基础信息新增
        int insert = productMapper.insert(product);
        if (insert == 0) {
            throw new ServiceException("发布失败！", SystemCode.DATA_ADD_FAILED.getCode());
        }

        //商品属性信息新增
        List<ProductAttributeDto> productAttributeDtos = productDto.getProductAttributes();
        if (CollectionUtil.isNotEmpty(productAttributeDtos)) {
            addProductAttributeList(productAttributeDtos, product.getId());
        }

        //新增商品展示分类信息
        List<ProductShowCategoryDto> productShowCategoryDtos = productDto.getProductShowCategorys();
        if (CollectionUtil.isNotEmpty(productShowCategoryDtos)) {
            addProductShowCategoryList(productShowCategoryDtos, product.getId());
        }

        //商品sku信息新增
        List<SkuStockDto> skuStockDtos = productDto.getSkuStocks();
        if (CollectionUtil.isNotEmpty(skuStockDtos)) {
            addSkuStock(skuStockDtos, product.getId());
        }

        //新增后商品id赋值
        productDto.setId(product.getId());
        productDto.setSkuStocks(skuStockDtos);

        //更新缓存商品基础信息
        updateCacheShoppingCartProduct(productDto);
    }

    /**
     * 商品展示分类批量变更
     *
     * @param productShowCategoryDtos
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProductShowCategory(List<ProductShowCategoryDto> productShowCategoryDtos, Long[] productIds) {
        Arrays.stream(productIds).forEach(productId -> {
            //删除展示分类信息
            productShowCategoryMapper.delete(new QueryWrapper<ProductShowCategory>().eq("product_id", productId));
            addProductShowCategoryList(productShowCategoryDtos, productId);
        });
    }

    /**
     * 商品专区变更 同时商品状态更新成已下架
     * 判断变更的专区是否有原先选择的展示分类
     * 如果一级分类不存在则直接新增一个一级分类再关联 有二级就再关联二级
     * 如果一级分类存在则直接关联 再看二级分类是否存在 存在则直接关联 不存在则新增一个二级分类在关联
     * <p>
     * 分拣品类跟商品一一对应 所以直接判断新专区是否有该分拣品类 有就直接关联，没有就插入一条再关联
     *
     * @param ids
     * @param saleMode
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProductSaleMode(Long[] ids, Long saleMode) {
        List<Long> list = Arrays.asList(ids);
        list.stream().forEach(id -> {
            ProductVo productVo = productMapper.querySaleModeChangeProduct(id);
            if (BeanUtil.isEmpty(productVo)) {
                throw new ServiceException("有商品不存在或已删除,请重新选择！", SystemCode.DATA_EXISTED.getCode());
            }
            //展示分类关联
            if (CollectionUtil.isNotEmpty(productVo.getProductShowCategorys())) {
                //先删除商品关联的展示分类
                productShowCategoryMapper.delete(new QueryWrapper<ProductShowCategory>().eq("product_id", id));
                List<ProductShowCategoryVo> productShowCategoryVos = productVo.getProductShowCategorys();
                for (ProductShowCategoryVo productShowCategoryVo : productShowCategoryVos) {
                    ShowCategory showCategory = showCategoryMapper.selectOne(new QueryWrapper<ShowCategory>().eq("name", productShowCategoryVo.getName()).eq("sale_mode", saleMode));
                    if (BeanUtil.isEmpty(showCategory)) {
                        //说明新专区不存在该展示分类 则先添加新的一级与二级展示分类再关联
                        showCategory = new ShowCategory();
                        showCategory.setParentId(0L);
                        showCategory.setSort(CommonConstants.NUMBER_ZERO);
                        showCategory.setName(productShowCategoryVo.getName());
                        showCategory.setSaleMode(saleMode);
                        int showCategoryInsert = showCategoryMapper.insert(showCategory);
                        if (showCategoryInsert == 0) {
                            throw new ServiceException("一级展示分类新增失败！", SystemCode.DATA_ADD_FAILED.getCode());
                        }
                        ProductShowCategory productShowCategory = new ProductShowCategory();
                        productShowCategory.setProductId(id);
                        productShowCategory.setParentId(0L);
                        productShowCategory.setShowCategoryId(showCategory.getId());
                        int productShowCategoryInsert = productShowCategoryMapper.insert(productShowCategory);
                        if (productShowCategoryInsert == 0) {
                            throw new ServiceException("商品一级展示分类新增失败！", SystemCode.DATA_ADD_FAILED.getCode());
                        }
                        List<ProductShowCategorySecondVo> productShowCategorySeconds = productShowCategoryVo.getProductShowCategorySeconds();
                        if (CollectionUtil.isEmpty(productShowCategorySeconds)) {
                            throw new ServiceException("一级展示分类下暂无二级分类, 请先完善该商品分类信息！", SystemCode.DATA_ADD_FAILED.getCode());
                        }
                        for (ProductShowCategorySecondVo productShowCategorySecondVo : productShowCategorySeconds) {
                            //新增展示分类
                            changeShowCategoryBySaleMode(showCategory, saleMode, productShowCategorySecondVo, id, productShowCategory);
                        }
                    } else {
                        //说明新专区存在该分类 再判断二级分类是否存在 存在直接关联 不存在新增后再插入
                        ProductShowCategory productShowCategory = new ProductShowCategory();
                        productShowCategory.setProductId(id);
                        productShowCategory.setParentId(0L);
                        productShowCategory.setShowCategoryId(showCategory.getId());
                        int productShowCategoryInsert = productShowCategoryMapper.insert(productShowCategory);
                        if (productShowCategoryInsert == 0) {
                            throw new ServiceException("商品一级展示分类新增失败！", SystemCode.DATA_ADD_FAILED.getCode());
                        }
                        List<ProductShowCategorySecondVo> productShowCategorySeconds = productShowCategoryVo.getProductShowCategorySeconds();
                        if (CollectionUtil.isEmpty(productShowCategorySeconds)) {
                            throw new ServiceException("一级展示分类下暂无二级分类, 请先完善该商品分类信息！", SystemCode.DATA_ADD_FAILED.getCode());
                        }
                        for (ProductShowCategorySecondVo productShowCategorySecondVo : productShowCategorySeconds) {
                            ShowCategory searchSecondShowCategory = showCategoryMapper.selectOne(new QueryWrapper<ShowCategory>().eq("name", productShowCategorySecondVo.getName()).eq("parent_id", showCategory.getId()).eq("sale_mode", saleMode));
                            if (BeanUtil.isEmpty(searchSecondShowCategory)) {
                                //新增展示分类
                                changeShowCategoryBySaleMode(showCategory, saleMode, productShowCategorySecondVo, id, productShowCategory);
                            } else {
                                //新增商品二级展示分类 记得关联上面商品一级展示分类id
                                ProductShowCategory secondProductShowCategory = new ProductShowCategory();
                                secondProductShowCategory.setProductId(id);
                                secondProductShowCategory.setParentId(productShowCategory.getId());
                                secondProductShowCategory.setShowCategoryId(searchSecondShowCategory.getId());
                                int secondProductShowCategoryInsert = productShowCategoryMapper.insert(secondProductShowCategory);
                                if (secondProductShowCategoryInsert == 0) {
                                    throw new ServiceException("商品二级展示分类新增失败！", SystemCode.DATA_ADD_FAILED.getCode());
                                }
                            }
                        }
                    }
                }
            }
            boolean sign = new LambdaUpdateChainWrapper<>(productMapper)
                    .eq(Product::getId, id)
                    .set(Product::getSaleMode, saleMode)
                    .set(Product::getStatus, ProductStatusEnum.SELL_OFF.getStatus()).update();
            if (!sign) {
                throw new ServiceException("商品专区变更失败！", SystemCode.DATA_UPDATE_FAILED.getCode());
            }
        });
        //更新购物车缓存商品状态与专区
        updateCacheShoppingCartProductSaleMode(ids, saleMode);
        //获取当前线程中的租户id的店铺信息;getTemplateCodeEnum 获取当前店铺使用模板类型
        CurShopInfoDto tenantIdShopInfo = CurUserUtil.getTenantIdShopInfo();
    }

    /**
     * 变更的专区没有的展示分类新增
     *
     * @param showCategory
     * @param saleMode                    专区
     * @param productShowCategorySecondVo
     * @param id                          商品id
     * @param productShowCategory
     */
    public void changeShowCategoryBySaleMode(ShowCategory showCategory, Long saleMode, ProductShowCategorySecondVo productShowCategorySecondVo, Long id, ProductShowCategory productShowCategory) {
        //新增二级展示分类 记得关联上面一级展示分类id
        ShowCategory secondShowCategory = new ShowCategory();
        secondShowCategory.setParentId(showCategory.getId());
        secondShowCategory.setSort(CommonConstants.NUMBER_ZERO);
        secondShowCategory.setName(productShowCategorySecondVo.getName());
        secondShowCategory.setSaleMode(saleMode);
        int secondShowCategoryInsert = showCategoryMapper.insert(secondShowCategory);
        if (secondShowCategoryInsert == 0) {
            throw new ServiceException("二级展示分类新增失败！", SystemCode.DATA_ADD_FAILED.getCode());
        }
        //新增商品二级展示分类 记得关联上面商品一级展示分类id
        ProductShowCategory secondProductShowCategory = new ProductShowCategory();
        secondProductShowCategory.setProductId(id);
        secondProductShowCategory.setParentId(productShowCategory.getId());
        secondProductShowCategory.setShowCategoryId(secondShowCategory.getId());
        int secondProductShowCategoryInsert = productShowCategoryMapper.insert(secondProductShowCategory);
        if (secondProductShowCategoryInsert == 0) {
            throw new ServiceException("商品二级展示分类新增失败！", SystemCode.DATA_ADD_FAILED.getCode());
        }
    }



    /**
     * 商品信息的修改（只有下架状态的商品才可以修改）
     * 修改商品信息，先清除商品原先的运费模版、sku、商品属性、展示分类信息，再执行插入操作
     *
     * @param productDto dto对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProduct(ProductDto productDto) {

        //判断商品是否处于下架状态
        Product productSearch = productMapper.selectById(productDto.getId());
        if (BeanUtil.isEmpty(productSearch)) {
            throw new ServiceException("商品不存在！", SystemCode.DATA_EXISTED.getCode());
        }

        //商品基础信息修改
        Product product = productDto.coverProduct();
        int update = productMapper.updateById(product);
        if (update == 0) {
            throw new ServiceException("修改失败！", SystemCode.DATA_UPDATE_FAILED.getCode());
        }

        //商品属性信息修改(先删除再新增)
        productAttributeMapper.delete(new QueryWrapper<ProductAttribute>().eq("product_id", productDto.getId()));
        List<ProductAttributeDto> productAttributeDtos = productDto.getProductAttributes();
        if (CollectionUtil.isNotEmpty(productAttributeDtos)) {
            addProductAttributeList(productAttributeDtos, productDto.getId());
        }

        //商品展示分类删除
        productShowCategoryMapper.delete(new QueryWrapper<ProductShowCategory>().eq("product_id", productDto.getId()));
        //商品展示分类信息新增
        List<ProductShowCategoryDto> productShowCategoryDtos = productDto.getProductShowCategorys();
        if (CollectionUtil.isNotEmpty(productShowCategoryDtos)) {
            //新增展示分类信息
            addProductShowCategoryList(productShowCategoryDtos, productDto.getId());
        }

        //更新sku信息(
        List<SkuStockDto> skuStockDtos = productDto.getSkuStocks();
        if (CollectionUtil.isNotEmpty(skuStockDtos)) {
            updateSkuStockByProduct(skuStockDtos, product.getId());
        }

        //更新缓存商品基础信息
        updateCacheShoppingCartProduct(productDto);
    }

    /**
     * 商品上下架
     *
     * @param ids
     * @param status
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProductStatus(Long[] ids, Integer status) {
        List<Long> list = Arrays.asList(ids);
        list.stream().forEach(id -> {
            boolean sign = new LambdaUpdateChainWrapper<>(productMapper)
                    .eq(Product::getId, id)
                    .set(Product::getStatus, status).update();
            if (!sign) {
                throw new ServiceException("商品状态更新失败！", SystemCode.DATA_UPDATE_FAILED.getCode());
            }
        });
        //更新缓存商品基础信息中的状态
        updateCacheShoppingCartProductStatus(ids, status);
        //获取当前线程中的租户id的店铺信息;getTemplateCodeEnum 获取当前店铺使用模板类型
        CurShopInfoDto tenantIdShopInfo = CurUserUtil.getTenantIdShopInfo();
    }

    /**
     * 商品属性批量新增
     *
     * @param productAttributeDtos
     * @param id
     */
    private void addProductAttributeList(List<ProductAttributeDto> productAttributeDtos, Long id) {
        productAttributeDtos.stream().forEach(bean -> {
            ProductAttribute productAttribute = bean.coverProductAttribute();
            productAttribute.setProductId(id);
            int insertProductAttribute = productAttributeMapper.insert(productAttribute);
            if (insertProductAttribute == 0) {
                throw new ServiceException("商品属性信息新增失败！", SystemCode.DATA_ADD_FAILED.getCode());
            }
        });
    }

    /**
     * 商品信息的删除
     *
     * @param ids  删除的商品对象集
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProductList(Long[] ids) {
        productAttributeMapper.delete(new QueryWrapper<ProductAttribute>().in("product_id", Arrays.asList(ids)));
        //需要清除sku缓存
        List<SkuStock> skuStocks = skuStockMapper.selectList(new QueryWrapper<SkuStock>().in("product_id", Arrays.asList(ids)));
        if (CollectionUtil.isNotEmpty(skuStocks)) {
            deleteSkuStock(skuStocks);
        }
        productShowCategoryMapper.delete(new QueryWrapper<ProductShowCategory>().in("product_id", Arrays.asList(ids)));
        removeByIds(Arrays.asList(ids));
        //删除缓存里面的商品信息
        deleteCacheShoppingCartProduct(ids);
        //获取当前线程中的租户id的店铺信息;getTemplateCodeEnum 获取当前店铺使用模板类型
        CurShopInfoDto tenantIdShopInfo = CurUserUtil.getTenantIdShopInfo();
    }

    /**
     * 商品展示分类大类批量新增
     *
     * @param productShowCategoryDtos
     * @param id
     */
    private void addProductShowCategoryList(List<ProductShowCategoryDto> productShowCategoryDtos, Long id) {
        productShowCategoryDtos.stream().forEach(bean -> {
            ProductShowCategory productShowCategory = bean.coverProductShowCategory();
            productShowCategory.setProductId(id);
            int insertSkuStock = productShowCategoryMapper.insert(productShowCategory);
            if (insertSkuStock == 0) {
                throw new ServiceException("商品展示分类信息新增失败！", SystemCode.DATA_ADD_FAILED.getCode());
            }
            //新增会员价信息
            if (CollectionUtil.isNotEmpty(bean.getProductShowCategorySeconds())) {
                addProductShowCategorySecondList(bean.getProductShowCategorySeconds(), productShowCategory.getId(), id);
            }
        });
    }

    /**
     * 商品展示分类二级分类批量新增
     *
     * @param productShowCategorySecondDtos
     * @param id
     */
    private void addProductShowCategorySecondList(List<ProductShowCategorySecondDto> productShowCategorySecondDtos, Long parentId, Long id) {
        productShowCategorySecondDtos.stream().forEach(bean -> {
            ProductShowCategory productShowCategory = bean.coverProductShowCategory();
            productShowCategory.setProductId(id);
            productShowCategory.setParentId(parentId);
            int insertSkuStock = productShowCategoryMapper.insert(productShowCategory);
            if (insertSkuStock == 0) {
                throw new ServiceException("商品展示二级分类信息新增失败！", SystemCode.DATA_ADD_FAILED.getCode());
            }
        });
    }




    /**
     * 已发货的订单商品移除列表
     *
     * @param discountProductParam
     * @return 商品list对象
     */
    @Override
    public IPage<DiscountProductVo> getRemoveProductList(DiscountProductParam discountProductParam) {
        //获取已发货的订单商品list
        List<Long> productIds = remoteOrderService.waitSendProduct(discountProductParam.getDeliverId());
        log.debug("-----" + discountProductParam.getDeliverId() + "------获取发货单的商品信息：" + productIds);
        IPage<DiscountProductVo> page = new Page<>(discountProductParam.getCurrent(), discountProductParam.getSize());
        if (CollectionUtil.isNotEmpty(productIds)) {
            discountProductParam.setProductIds(productIds);
            page.setRecords(discountProductMapper.queryRemoveProductList(page, discountProductParam));
        }
        return page;
    }


    /**
     * 根据商品数组匹配未删除的商品
     *
     * @param ids
     * @return List<DiscountProductVo>
     */
    @Override
    public List<DiscountProductVo> getAliveProductList(Long[] ids) {
        return discountProductMapper.querySaveProductList(Arrays.asList(ids));
    }


    /**
     * 批量查询多个商品详情
     *
     * @param productIds
     * @return List<DiscountProductVo>
     */
    @Override
    public List<DiscountProductVo> getDiscountProductTypeList(List<Long> productIds) {
        return discountProductMapper.queryDiscountProductTypeList(productIds);
    }

    /**
     * 查询上架商品的数量
     *
     * @return Integer
     */
    @Override
    public Integer getProductCount() {
        return discountProductMapper.selectCount(new QueryWrapper<Product>().eq("status", CommonConstants.NUMBER_ONE).eq("is_deleted", CommonConstants.NUMBER_ZERO));
    }


    /**
     * 查询运费模版id是否被商品使用
     *
     * @param templateId
     * @return Boolean
     */
    @Override
    public Boolean checkProductByTemplateId(Long templateId) {
        int count = productMapper.selectCount(new QueryWrapper<Product>().eq("freight_template_id", templateId).eq("is_deleted", CommonConstants.NUMBER_ZERO));
        return count > 0;
    }


    /**
     * 获取单个sku关联商品会员价信息
     *
     * @param skuIds
     * @return ItemVo
     */
    @Override
    public List<ItemVo> findItemVoByIds(List<Long> skuIds) {
        if (skuIds.isEmpty()) {
            return new ArrayList<>(CommonConstants.NUMBER_ZERO);
        } else {
            return skuStockMapper.queryItemVoByIds(skuIds);
        }
    }

    /**
     * 产品修改调用更新sku库存(包括sku表、缓存维护的sku信息、购物车sku信息)
     *
     * @param skuStockDtos
     * @param productId
     */
    public void updateSkuStockByProduct(List<SkuStockDto> skuStockDtos, Long productId) {
        List<SkuStock> skuStocks = skuStockMapper.selectList(new QueryWrapper<SkuStock>().eq("product_id", productId));
        //需要新增加的sku信息
        List<SkuStockDto> distinctSkuStockDtos = skuStockDtos.stream()
                .filter(skuStockDto -> !skuStocks.stream()
                        .map(skuStock -> skuStock.getId())
                        .collect(toList())
                        .contains(skuStockDto.getId()))
                .collect(toList());
        if (CollectionUtil.isNotEmpty(distinctSkuStockDtos)) {
            addSkuStock(distinctSkuStockDtos, productId);
        }
        //需要删除的sku信息
        List<SkuStock> distinctSkuStocks = skuStocks.stream()
                .filter(skuStock -> !skuStockDtos.stream()
                        .map(skuStockDto -> skuStockDto.getId())
                        .collect(toList())
                        .contains(skuStock.getId()))
                .collect(toList());
        if (CollectionUtil.isNotEmpty(distinctSkuStocks)) {
            deleteSkuStock(distinctSkuStocks);
        }
        //需要修改的sku信息
        List<SkuStockDto> sameUserList = skuStockDtos.stream()
                .filter(skuStockDto -> skuStocks.stream()
                        .map(skuStock -> skuStock.getId())
                        .collect(toList())
                        .contains(skuStockDto.getId()))
                .collect(toList());
        if (CollectionUtil.isNotEmpty(sameUserList)) {
            updateSkuStock(sameUserList, productId);
        }
    }

    /**
     * 新增sku库存(包括sku表、缓存维护的sku信息、购物车sku信息)
     *
     * @param skuStockDtos 需要插入的sku库存list
     * @param productId
     */
    public void addSkuStock(List<SkuStockDto> skuStockDtos, Long productId) {
        GoodsSkuStockRedisKey goodsSkuStockRedisKey = new GoodsSkuStockRedisKey();
        skuStockDtos.stream().forEach(bean -> {
            bean.setProductId(productId);
            SkuStock skuStock = bean.coverSkuStock();
            //新增sku
            int insert = skuStockMapper.insert(skuStock);
            if (insert == 0) {
                throw new ServiceException("商品sku信息新增失败！", SystemCode.DATA_ADD_FAILED.getCode());
            }
            bean.setId(skuStock.getId());
            //新增sku缓存信息
            goodsSkuStockRedisKey.set(String.valueOf(skuStock.getId()), String.valueOf(bean.getStock()));
        });
    }

    /**
     * 更新购物车商品状态缓存信息
     *
     * @param ids
     */
    public void updateCacheShoppingCartProductStatus(Long[] ids, Integer status) {
        GoodsProductRedisKey goodsProductRedisKey = new GoodsProductRedisKey();
        Arrays.asList(ids).stream().forEach(id -> {
            ApiShoppingCartProductVo apiShoppingCartProductVo = JSON.parseObject(goodsProductRedisKey.get(String.valueOf(id)), ApiShoppingCartProductVo.class);
            if (!BeanUtil.isEmpty(apiShoppingCartProductVo)) {
                apiShoppingCartProductVo.setStatus(status);
                goodsProductRedisKey.set(String.valueOf(id), JSON.toJSONString(apiShoppingCartProductVo));
            }
        });
    }

    /**
     * 更新购物车商品专区缓存信息
     *
     * @param ids
     */
    public void updateCacheShoppingCartProductSaleMode(Long[] ids, Long saleMode) {
        GoodsProductRedisKey goodsProductRedisKey = new GoodsProductRedisKey();
        Arrays.asList(ids).stream().forEach(id -> {
            ApiShoppingCartProductVo apiShoppingCartProductVo = JSON.parseObject(goodsProductRedisKey.get(String.valueOf(id)), ApiShoppingCartProductVo.class);
            if (!BeanUtil.isEmpty(apiShoppingCartProductVo)) {
                apiShoppingCartProductVo.setSaleMode(saleMode);
                apiShoppingCartProductVo.setStatus(CommonConstants.NUMBER_ZERO);
                goodsProductRedisKey.set(String.valueOf(id), JSON.toJSONString(apiShoppingCartProductVo));
            }
        });
    }


    /**
     * 更新购物车商品缓存信息
     *
     * @param productDto
     */
    public void updateCacheShoppingCartProduct(ProductDto productDto) {
        GoodsProductRedisKey goodsProductRedisKey = new GoodsProductRedisKey();
        ApiShoppingCartProductVo apiShoppingCartProductVo = new ApiShoppingCartProductVo();
        apiShoppingCartProductVo.setProductId(productDto.getId());
        apiShoppingCartProductVo.setProductName(productDto.getName());
        apiShoppingCartProductVo.setProductSn(productDto.getProductSn());
        apiShoppingCartProductVo.setPic(productDto.getPic());
        apiShoppingCartProductVo.setStatus(productDto.getStatus());
        apiShoppingCartProductVo.setDistributionMode(productDto.getDistributionMode());
        apiShoppingCartProductVo.setSkuStocks(productDto.getSkuStocks());
        apiShoppingCartProductVo.setSaleMode(productDto.getSaleMode());
        apiShoppingCartProductVo.setDeleted(CommonConstants.NUMBER_ZERO);
        goodsProductRedisKey.set(String.valueOf(productDto.getId()), JSON.toJSONString(apiShoppingCartProductVo));
    }

    /**
     * 删除购物车商品缓存信息 更改删除状态的值
     *
     * @param ids
     */
    public void deleteCacheShoppingCartProduct(Long[] ids) {
        GoodsProductRedisKey goodsProductRedisKey = new GoodsProductRedisKey();
        Arrays.asList(ids).stream().forEach(id -> {
            ApiShoppingCartProductVo apiShoppingCartProductVo = JSON.parseObject(goodsProductRedisKey.get(String.valueOf(id)), ApiShoppingCartProductVo.class);
            if (!BeanUtil.isEmpty(apiShoppingCartProductVo)) {
                apiShoppingCartProductVo.setDeleted(CommonConstants.NUMBER_ONE);
                goodsProductRedisKey.set(String.valueOf(id), JSON.toJSONString(apiShoppingCartProductVo));
            }
        });
    }

    /**
     * 删除sku库存(包括sku表、缓存维护的sku信息、购物车sku信息)
     *
     * @param skuStocks 需要插入的sku库存list
     */
    public void deleteSkuStock(List<SkuStock> skuStocks) {
        GoodsSkuStockRedisKey goodsSkuStockRedisKey = new GoodsSkuStockRedisKey();
        skuStocks.stream().forEach(bean -> {
            //删除商品sku信息
            int delete = skuStockMapper.deleteById(bean.getId());
            if (delete == 0) {
                throw new ServiceException("商品sku信息删除失败！", SystemCode.DATA_DELETE_FAILED.getCode());
            }
            //删除sku缓存信息
            goodsSkuStockRedisKey.del(String.valueOf(bean.getId()));
        });
    }

    /**
     * 修改sku库存(包括sku表、缓存维护的sku信息、购物车sku信息)
     *
     * @param skuStockDtos 需要修改的sku库存list
     * @param productId
     */
    public void updateSkuStock(List<SkuStockDto> skuStockDtos, Long productId) {
        GoodsSkuStockRedisKey goodsSkuStockRedisKey = new GoodsSkuStockRedisKey();
        skuStockDtos.stream().forEach(bean -> {
            SkuStock skuStock = skuStockMapper.selectById(bean.getId());
            BeanUtil.copyProperties(bean, skuStock, "sale");
            //更新sku缓存信息
            int update = skuStockMapper.updateById(skuStock);
            if (update == 0) {
                throw new ServiceException("商品sku信息更新失败！", SystemCode.DATA_UPDATE_FAILED.getCode());
            }
            //更新sku缓存信息
            goodsSkuStockRedisKey.set(String.valueOf(bean.getId()), String.valueOf(bean.getStock()));
        });
    }

    /**
     * 新建店铺同时新增默认店铺里面的默认商品 新增商品信息同时插入运费模版、sku、商品属性、展示分类信息，同时清除缓存数据
     * 注：每次查库的时候把tenantId跟shopId设置为默认值 查完之后记得恢复 不然数据插入会出错
     *
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createDefaultProduct() {

        //全局定义请求的shopId跟tenantId
        String shopId = ShopContextHolder.getShopId();
        String tenantId = TenantContextHolder.getTenantId();
        //设置默认shopId跟tenantId
        ShopContextHolder.setShopId(GoodsConstant.DEFAULT_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.DEFAULT_TENANT_ID);
        //属性模版默认数据新增
        AttributeTemplateVo attributeTemplateVo = attributeTemplateMapper.queryDefaultAttributeTemplateList();
        Long attributeTemplateId = null;
        String attributeTemplateName = null;
        //恢复定义的请求的shopId跟tenantId 下面要插入数据
        if (!BeanUtil.isEmpty(attributeTemplateVo)) {
            ShopContextHolder.setShopId(shopId);
            TenantContextHolder.setTenantId(tenantId);
            AttributeTemplate attributeTemplate = new AttributeTemplate();
            BeanUtil.copyProperties(attributeTemplateVo, attributeTemplate);
            attributeTemplate.setId(null);
            attributeTemplateMapper.insert(attributeTemplate);
            List<AttributeTemplateSecondVo> attributeTemplates = attributeTemplateVo.getAttributeTemplates();
            if (CollectionUtil.isNotEmpty(attributeTemplates)) {
                for (AttributeTemplateSecondVo child : attributeTemplates) {
                    AttributeTemplate childNode = new AttributeTemplate();
                    BeanUtil.copyProperties(child, childNode);
                    childNode.setId(null);
                    childNode.setParentId(attributeTemplate.getId());
                    attributeTemplateMapper.insert(childNode);
                    attributeTemplateId = childNode.getId();
                    attributeTemplateName = childNode.getName();
                }
            } else {
                attributeTemplateId = attributeTemplate.getId();
                attributeTemplateName = attributeTemplate.getName();
            }
        }
        //设置默认shopId跟tenantId
        ShopContextHolder.setShopId(GoodsConstant.DEFAULT_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.DEFAULT_TENANT_ID);
        //恢复定义的请求的shopId跟tenantId 下面要插入数据
        ShopContextHolder.setShopId(shopId);
        TenantContextHolder.setTenantId(tenantId);

        //设置默认shopId跟tenantId
        ShopContextHolder.setShopId(GoodsConstant.DEFAULT_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.DEFAULT_TENANT_ID);

        //设置默认shopId跟tenantId
        ShopContextHolder.setShopId(GoodsConstant.DEFAULT_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.DEFAULT_TENANT_ID);
        //商品专区默认数据新增
        List<SaleModeVo> saleModeVos = saleModeMapper.queryDefaultSaleMode();
        //恢复定义的请求的shopId跟tenantId 下面要插入数据
        ShopContextHolder.setShopId(shopId);
        TenantContextHolder.setTenantId(tenantId);
        Long saleModeId = 0L;
        log.warn(ShopContextHolder.getShopId() + "shopId=============>");
        log.warn(TenantContextHolder.getTenantId() + "TenantId=============>");
        for (SaleModeVo saleModeVo : saleModeVos) {
            //获取当前线程中的租户id的店铺信息;getTemplateCodeEnum 获取当前店铺使用模板类型
            CurShopInfoDto tenantIdShopInfo = CurUserUtil.getTenantIdShopInfo();
            SaleMode saleMode = new SaleMode();
            BeanUtil.copyProperties(saleModeVo, saleMode);
            saleMode.setId(null);
            saleModeMapper.insert(saleMode);
            saleModeId = saleMode.getId();
        }

        //设置默认shopId跟tenantId
        ShopContextHolder.setShopId(GoodsConstant.DEFAULT_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.DEFAULT_TENANT_ID);
        //展示分类默认数据新增
        ShowCategoryVo showCategoryVo = showCategoryMapper.queryDefaultShowCategory();
        //恢复定义的请求的shopId跟tenantId 下面要插入数据
        ShopContextHolder.setShopId(shopId);
        TenantContextHolder.setTenantId(tenantId);
        ShowCategory showCategory = new ShowCategory();
        BeanUtil.copyProperties(showCategoryVo, showCategory);
        showCategory.setId(null);
        showCategory.setLevel(CommonConstants.NUMBER_ZERO);
        showCategory.setSaleMode(saleModeId);
        showCategoryMapper.insert(showCategory);
        List<ShowCategorySecondVo> showCategoryVos = showCategoryVo.getShowCategoryVos();
        Long showCategoryId = null;
        if (CollectionUtil.isNotEmpty(showCategoryVos)) {
            for (ShowCategorySecondVo child : showCategoryVos) {
                ShowCategory childNode = new ShowCategory();
                BeanUtil.copyProperties(child, childNode);
                childNode.setId(null);
                childNode.setParentId(showCategory.getId());
                childNode.setLevel(CommonConstants.NUMBER_ONE);
                childNode.setSaleMode(saleModeId);
                showCategoryMapper.insert(childNode);
                showCategoryId = childNode.getId();
            }
        } else {
            showCategoryId = showCategory.getId();
        }
        //商品默认数据新增
        ShopContextHolder.setShopId(GoodsConstant.DEFAULT_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.DEFAULT_TENANT_ID);
        List<ProductVo> productVos = this.productMapper.queryDefaultProduct();
        ShopContextHolder.setShopId(shopId);
        TenantContextHolder.setTenantId(tenantId);
        if (CollectionUtil.isNotEmpty(productVos)) {
            for (ProductVo productVo : productVos) {
                //状态默认上架
                Product product = new Product();
                BeanUtil.copyProperties(productVo, product);
                product.setId(null);
                product.setCreateTime(null);
                product.setUpdateTime(null);
                product.setAttributeId(attributeTemplateId);
                product.setAttributeName(attributeTemplateName);
                product.setSaleMode(saleModeId);
                //商品基础信息新增
                int insert = productMapper.insert(product);
                if (insert == 0) {
                    throw new ServiceException("发布失败！", SystemCode.DATA_ADD_FAILED.getCode());
                }
                //商品关联展示分类信息新增
                ProductShowCategory productShowCategory = new ProductShowCategory();
                productShowCategory.setProductId(product.getId());
                productShowCategory.setParentId(0L);
                productShowCategory.setShowCategoryId(showCategory.getId());
                productShowCategoryMapper.insert(productShowCategory);
                if (CollectionUtil.isNotEmpty(showCategoryVos)) {
                    ProductShowCategory childNode = new ProductShowCategory();
                    childNode.setProductId(product.getId());
                    childNode.setShowCategoryId(showCategoryId);
                    childNode.setParentId(productShowCategory.getId());
                    productShowCategoryMapper.insert(childNode);
                }
                //商品关联属性信息新增
                List<ProductAttributeVo> productAttributes = productVo.getProductAttributes();
                if (CollectionUtil.isNotEmpty(productAttributes)) {
                    for (ProductAttributeVo productAttributeVo : productAttributes) {
                        ProductAttribute productAttribute = new ProductAttribute();
                        BeanUtil.copyProperties(productAttributeVo, productAttribute);
                        productAttribute.setProductId(product.getId());
                        productAttribute.setId(null);
                        productAttributeMapper.insert(productAttribute);
                    }
                }
                //商品sku库存与会员价信息新增
                List<SkuStockVo> skuStocks = productVo.getSkuStocks();
                //方便商品基础信息缓存存储组装skuStock数据
                List<SkuStockDto> skuStockDtos = new ArrayList<>(skuStocks.size());
                if (CollectionUtil.isNotEmpty(skuStocks)) {
                    skuStocks.stream().forEach(skuStockVo -> {
                        //新增sku库存信息
                        SkuStock skuStock = new SkuStock();
                        BeanUtil.copyProperties(skuStockVo, skuStock);
                        skuStock.setProductId(product.getId());
                        skuStock.setId(null);
                        skuStockMapper.insert(skuStock);
                        SkuStockDto skuStockDto = new SkuStockDto();
                        BeanUtil.copyProperties(skuStock, skuStockDto);
                        skuStockDtos.add(skuStockDto);
                    });
                }
                //更新缓存商品基础信息
                GoodsProductRedisKey goodsProductRedisKey = new GoodsProductRedisKey();
                ApiShoppingCartProductVo apiShoppingCartProductVo = new ApiShoppingCartProductVo();
                apiShoppingCartProductVo.setProductId(product.getId());
                apiShoppingCartProductVo.setProductName(product.getName());
                apiShoppingCartProductVo.setProductSn(product.getProductSn());
                apiShoppingCartProductVo.setPic(product.getPic());
                apiShoppingCartProductVo.setStatus(product.getStatus());
                apiShoppingCartProductVo.setDistributionMode(product.getDistributionMode());
                apiShoppingCartProductVo.setSkuStocks(skuStockDtos);
                apiShoppingCartProductVo.setSaleMode(product.getSaleMode());
                apiShoppingCartProductVo.setDeleted(CommonConstants.NUMBER_ZERO);
                goodsProductRedisKey.set(String.valueOf(product.getId()), JSON.toJSONString(apiShoppingCartProductVo));
            }
        }

    }

    /**
     * 生成商品货号公共方法
     *
     * @return Result
     */
    public String getProductSn() {
        String productSn;
        do {
            //生成一个16位的商品id
            String date = DateUtil.format(new Date(), new SimpleDateFormat("yyyyMMdd"));
            Integer count = this.baseMapper.queryAllCount() + CommonConstants.NUMBER_ONE;
            String newCount = new DecimalFormat("00000000").format(count);
            productSn = date + newCount;
            //验证改id是否已经被使用
            Product productSearch = this.baseMapper.selectOne(new QueryWrapper<Product>().eq("product_sn", productSn));
            if (BeanUtil.isEmpty(productSearch)) {
                break;
            }
        } while (true);
        return productSn;
    }

    /**
     * 淘宝商品csv文件导入
     *
     * @param file 商品csv文件
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result importCsvProduct(MultipartFile file) {
        if (file.getSize() == 0) {
            throw new ServiceException("文件表格异常", SystemCode.DATA_EXISTED_CODE);
        }
        //获取文件名
        String fileName = file.getOriginalFilename();
        //获取文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (".csv".equals(suffix)) {
            try {
                CsvReader reader = CsvUtil.getReader();
                //从文件中读取CSV数据
                CsvData data = reader.read(FileUtil.file(CsvFileUtil.multipartFileToFile(file)), Charset.forName("utf-16"));
                if (data.getRowCount() < CommonConstants.NUMBER_FOUR) {
                    throw new ServiceException("文件内容有误！", SystemCode.DATA_ADD_FAILED_CODE);
                }
                //CsvData对象转成list对象
                List<Map<String, Object>> productList = CsvFileUtil.transCsvRowToList(data);
                for (Map<String, Object> obj : productList) {
                    Product product = new Product();
                    //调用生成货号方法生成商品货号
                    String productSn = getProductSn();
                    product.setProductSn(productSn);
                    product.setName(Convert.toStr(obj.get("title")).replaceAll("\"", ""));
                    product.setDetail(Convert.toStr(obj.get("description")).replaceAll("\"", ""));
                    product.setDistributionMode(CommonConstants.NUMBER_ZERO);
                    product.setStatus(CommonConstants.NUMBER_ZERO);
                    product.setScore(BigDecimal.valueOf(5.0));
                    product.setCsvUrl("https://item.taobao.com/item.htm?id=" + Convert.toStr(obj.get("num_id")).replaceAll("\"", ""));
                    //状态设置为素材库中
                    product.setPlace(CommonConstants.NUMBER_ONE);
                    //图片赋值公共方法
                    setCsvPic(product, obj);
                    //sku信息组合
                    List<SkuStock> skuStocks = new ArrayList<>(20);
                    //sku赋值公共方法
                    setCsvSku(product, skuStocks, obj);
                    //商品主表信息新增
                    int insert = productMapper.insert(product);
                    if (insert == 0) {
                        throw new ServiceException("商品信息上传失败！", SystemCode.DATA_ADD_FAILED_CODE);
                    }
                    //sku信息新增
                    if (CollectionUtil.isNotEmpty(skuStocks)) {
                        skuStocks.stream().forEach(bean -> {
                            bean.setProductId(product.getId());
                            int skuStockInsert = skuStockMapper.insert(bean);
                            if (skuStockInsert == 0) {
                                throw new ServiceException("库存信息上传失败！", SystemCode.DATA_ADD_FAILED_CODE);
                            }
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException("文件解析失败！", SystemCode.DATA_ADD_FAILED_CODE);
            }
        } else {
            throw new ServiceException("文件格式有误,请重新上传!", SystemCode.PARAM_TYPE_ERROR_CODE);
        }
        return Result.ok();
    }

    /**
     * 图片赋值公共方法
     *
     * @param product
     * @param obj
     */
    public void setCsvPic(Product product, Map<String, Object> obj) {
        //商品主图字段赋值
        String picString = Convert.toStr(obj.get("picture"));
        if (StringUtil.isNotEmpty(picString)) {
            String[] picStrArray = picString.replaceAll("\"", "").split(";");
            List<String> picList = new ArrayList<>(picStrArray.length);
            Arrays.stream(picStrArray).forEach(picStr -> {
                picStr = picStr.replaceAll("\'", "");
                if (StringUtil.isNotEmpty(picStr) && !"''".equals(picStr)) {
                    String[] picArray = picStr.split("\\|");
                    //取的商品主图信息 过滤掉sku图片信息
                    String[] str = picArray[0].split(":");
                    if (str.length < CommonConstants.NUMBER_FOUR) {
                        String pic = picArray[1];
                        if (StringUtil.isNotEmpty(pic)) {
                            picList.add(pic);
                        }
                    }
                }
            });
            if (CollectionUtil.isNotEmpty(picList)) {
                //调用图片上传服务上传图片并获取返回的图片路径
                List<String> pictures = remoteSysOssService.batchDownload(picList);
                if (CollectionUtil.isNotEmpty(pictures)) {
                    String picture = CollectionUtil.join(pictures, ",");
                    product.setPic(pictures.get(CommonConstants.NUMBER_ZERO));
                    product.setAlbumPics(picture);
                }
            }
        }
    }

    /**
     * sku赋值公共方法 同时商品主表限购类型赋值
     *
     * @param product
     * @param skuStocks
     * @param obj
     */
    public void setCsvSku(Product product, List<SkuStock> skuStocks, Map<String, Object> obj) {
        String skuString = Convert.toStr(obj.get("skuProps")).replaceAll("\"", "");
        if (StringUtil.isNotEmpty(skuString)) {
            //限购类型赋值
            product.setLimitType(CommonConstants.NUMBER_ONE);
            //多规格sku信息新增
            String[] skuArray = skuString.split(";");
            Arrays.stream(skuArray).forEach(sku -> {
                if (sku.contains("::")) {
                    String price = sku.split(":")[0];
                    String stock = sku.split(":")[1];
                    SkuStock skuStock = new SkuStock();
                    if (StringUtil.isNotEmpty(price)) {
                        skuStock.setOriginalPrice(Convert.toBigDecimal(price));
                    }
                    if (StringUtil.isNotEmpty(stock)) {
                        skuStock.setStock(Convert.toInt(stock));
                    }
                    skuStocks.add(skuStock);
                }
            });
        } else {
            //限购类型赋值
            product.setLimitType(CommonConstants.NUMBER_ZERO);
            //单规格sku信息新增
            SkuStock skuStock = new SkuStock();
            if (obj.get("price") != null) {
                skuStock.setOriginalPrice(Convert.toBigDecimal(obj.get("price")));
            }
            if (obj.get("num") != null) {
                skuStock.setStock(Convert.toInt(obj.get("num")));
            }
            skuStocks.add(skuStock);
        }
    }

    /**
     * 获取素材库商品分页信息
     *
     * @param productParam
     * @return 商品分页对象
     */
    @Override
    public IPage<ProductVo> getCsvProductList(ProductParam productParam) {
        IPage<ProductVo> page = new Page<>(productParam.getCurrent(), productParam.getSize());
        List<ProductVo> productVos = csvProductMapper.queryCsvProductList(page, productParam);
        return page.setRecords(productVos);
    }
}
