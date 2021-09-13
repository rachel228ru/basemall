package com.medusa.gruul.goods.service.api.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.CurUserUtil;
import com.medusa.gruul.common.core.util.StringUtil;
import com.medusa.gruul.common.core.util.SystemCode;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.common.dto.CurShopInfoDto;
import com.medusa.gruul.common.dto.CurUserDto;
import com.medusa.gruul.goods.api.constant.GoodsProductRedisKey;
import com.medusa.gruul.goods.api.constant.GoodsSkuStockRedisKey;
import com.medusa.gruul.goods.api.constant.ShoppingCartRedisKey;
import com.medusa.gruul.goods.api.entity.SaleMode;
import com.medusa.gruul.goods.api.entity.ShoppingCart;
import com.medusa.gruul.goods.api.model.dto.api.ApiShoppingCartDto;
import com.medusa.gruul.goods.api.model.dto.manager.SkuStockDto;
import com.medusa.gruul.goods.api.model.vo.api.ApiShoppingCartProductVo;
import com.medusa.gruul.goods.api.model.vo.api.ApiShoppingCartVo;
import com.medusa.gruul.goods.mapper.api.ApiShoppingCartMapper;
import com.medusa.gruul.goods.mq.Sender;
import com.medusa.gruul.goods.mq.ShoppingCartMessage;
import com.medusa.gruul.goods.service.api.IApiShoppingCartService;
import com.medusa.gruul.goods.util.SortUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 小程序购物车信息 服务实现类
 *
 * @author lcy
 * @since 2019-10-15
 */
@Service
public class ApiShoppingCartServiceImpl extends ServiceImpl<ApiShoppingCartMapper, ShoppingCart> implements IApiShoppingCartService {

    @Resource
    private Sender sender;


    /**
     * 根据用户id查询购物车信息
     * @return 商品信息
     */
    @Override
    public List<ApiShoppingCartVo> getShoppingCartListByUserId() {
        CurUserDto curUserDto = CurUserUtil.getHttpCurUser();
        String userId = curUserDto.getUserId();
        String shopId = ShopContextHolder.getShopId();
        String tenantId = TenantContextHolder.getTenantId();
        return getData(userId, tenantId, shopId);
    }

    /**
     * 加入购物车 先更新缓存数据 再发消息更新数据表数据
     *
     * @param apiShoppingCartDto
     */
    @Override
    public void addShoppingCart(ApiShoppingCartDto apiShoppingCartDto) {
        CurUserDto curUserDto = CurUserUtil.getHttpCurUser();
        apiShoppingCartDto.setUserId(curUserDto.getUserId());
        String shopId = ShopContextHolder.getShopId();
        String tenantId = TenantContextHolder.getTenantId();
        ShoppingCartRedisKey shoppingCartRedisKey = new ShoppingCartRedisKey();
        Integer goodsNumber = apiShoppingCartDto.getGoodsNumber();
        //缓存中更新购物车信息
        updateShoppingCartCache(apiShoppingCartDto, Long.valueOf(CommonConstants.NUMBER_ZERO), shoppingCartRedisKey, tenantId, shopId);
        //消息队列发送
        ShoppingCartMessage shoppingCartMessage = new ShoppingCartMessage();
        shoppingCartMessage.setTenantId(tenantId);
        shoppingCartMessage.setShopId(shopId);
        //防止缓存更新把数量变更掉，在缓存更新完之后重新赋值
        apiShoppingCartDto.setGoodsNumber(goodsNumber);
        shoppingCartMessage.setApiShoppingCartDto(apiShoppingCartDto);
        shoppingCartMessage.setUserId(apiShoppingCartDto.getUserId());
        shoppingCartMessage.setSkuId(Long.valueOf(CommonConstants.NUMBER_ZERO));
        sender.sendShoppingCartMessage(shoppingCartMessage);
    }

    /**
     * 清空用户购物车失效商品
     *
     * @param ids 失效商品数组
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cleanEffectShoppingCart(Long[] ids) {
        CurUserDto curUserDto = CurUserUtil.getHttpCurUser();
        String userId = curUserDto.getUserId();
        ShoppingCartRedisKey shoppingCartRedisKey = new ShoppingCartRedisKey();
        List<Long> idList = Arrays.asList(ids);
        String shopId = ShopContextHolder.getShopId();
        String tenantId = TenantContextHolder.getTenantId();
        //用户购物车缓存key值 用户id+租户id+商户id
        String userKey = userId + tenantId + shopId;
        idList.forEach(id -> {
            //清除缓存购物车商品数据
            shoppingCartRedisKey.hdel(userKey, JSON.toJSONString(id));
            //删除数据库数据
            this.baseMapper.delete(new QueryWrapper<ShoppingCart>().eq("user_id", userId).eq("sku_id", id));
        });
    }

    /**
     * 切换购物车商品选中状态
     *
     * @param ids 切换的商品数组
     * @param selectStatus
     */
    @Override
    public void changeSelectStatus(Long[] ids, Integer selectStatus) {
        CurUserDto curUserDto = CurUserUtil.getHttpCurUser();
        String userId = curUserDto.getUserId();
        ShoppingCartRedisKey shoppingCartRedisKey = new ShoppingCartRedisKey();
        List<Long> idList = Arrays.asList(ids);
        String shopId = ShopContextHolder.getShopId();
        String tenantId = TenantContextHolder.getTenantId();
        //用户购物车缓存key值 用户id+租户id+商户id
        String userKey = userId + tenantId + shopId;
        idList.forEach(id -> {
            ApiShoppingCartVo apiShoppingCartVo = JSON.parseObject(shoppingCartRedisKey.hget(userKey, JSON.toJSONString(id)), ApiShoppingCartVo.class);
            if(!BeanUtil.isEmpty(apiShoppingCartVo)){
                apiShoppingCartVo.setSelectStatus(selectStatus);
            }
            shoppingCartRedisKey.hset(userKey, JSON.toJSONString(id), JSON.toJSONString(apiShoppingCartVo));
        });
    }

    /**
     * 修改购物车信息
     *
     * @param params 被修改前与修改后的购物车商品信息
     */
    @Override
    public void updateShoppingCart(Map<String, ApiShoppingCartDto> params) {
        ApiShoppingCartDto oldApiShoppingCartDto = params.get("oldApiShoppingCartDto");
        ApiShoppingCartDto newApiShoppingCartDto = params.get("newApiShoppingCartDto");
        CurUserDto curUserDto = CurUserUtil.getHttpCurUser();
        oldApiShoppingCartDto.setUserId(curUserDto.getUserId());
        newApiShoppingCartDto.setUserId(curUserDto.getUserId());
        String shopId = ShopContextHolder.getShopId();
        String tenantId = TenantContextHolder.getTenantId();
        Integer goodsNumber = newApiShoppingCartDto.getGoodsNumber();
        ShoppingCartRedisKey shoppingCartRedisKey = new ShoppingCartRedisKey();
        //缓存中更新购物车信息
        updateShoppingCartCache(newApiShoppingCartDto, oldApiShoppingCartDto.getSkuId(), shoppingCartRedisKey, tenantId, shopId);
        //消息队列发送
        ShoppingCartMessage shoppingCartMessage = new ShoppingCartMessage();
        shoppingCartMessage.setTenantId(tenantId);
        shoppingCartMessage.setShopId(shopId);
        //防止缓存更新把数量变更掉，在缓存更新完之后重新赋值
        newApiShoppingCartDto.setGoodsNumber(goodsNumber);
        shoppingCartMessage.setApiShoppingCartDto(newApiShoppingCartDto);
        shoppingCartMessage.setUserId(oldApiShoppingCartDto.getUserId());
        shoppingCartMessage.setSkuId(oldApiShoppingCartDto.getSkuId());
        sender.sendShoppingCartMessage(shoppingCartMessage);
    }

    /**
     * 更新购物车缓存数据公用方法
     *
     * @param apiShoppingCartDto
     * @param shoppingCartRedisKey
     * @param tenantId
     * @param shopId
     */
    public void updateShoppingCartCache(ApiShoppingCartDto apiShoppingCartDto, Long skuId, ShoppingCartRedisKey shoppingCartRedisKey, String tenantId, String shopId) {
        //用户购物车缓存key值 用户id+租户id+商户id
        String userKey = apiShoppingCartDto.getUserId() + tenantId + shopId;
        ApiShoppingCartVo apiShoppingCartVo = JSON.parseObject(shoppingCartRedisKey.hget(userKey, JSON.toJSONString(apiShoppingCartDto.getSkuId())), ApiShoppingCartVo.class);
        if (!BeanUtil.isEmpty(apiShoppingCartVo)) {
            //如果存在相同规格的商品，查询出来的商品数量累加
            List<SkuStockDto> skuStockDtos = apiShoppingCartDto.getSkuStocks();
            if (CollectionUtil.isNotEmpty(skuStockDtos)) {
                skuStockDtos.forEach(bean -> {
                    //判断新老数据sku是否一致（区分修改是否更换sku）
                    if (apiShoppingCartDto.getSkuId().equals(bean.getId())) {
                        int quantity = apiShoppingCartDto.getGoodsNumber();
                        if (!apiShoppingCartDto.getSkuId().equals(skuId)) {
                            quantity = quantity + apiShoppingCartVo.getGoodsNumber();
                        }
                        //限购数量验证
                        if (bean.getPerLimit() < quantity && !CommonConstants.NUMBER_ZERO.equals(bean.getPerLimit())) {
                            throw new ServiceException("数量超过限购数量！", SystemCode.DATA_UPDATE_FAILED.getCode());
                        } else {
                            apiShoppingCartDto.setGoodsNumber(quantity);
                        }
                    }
                });
            }
        }
        //设置购物车商品变动时间 为购物车列表排序用
        apiShoppingCartDto.setSortTime(new Date());
        //判断是加入购物车还是修改购物车规格 修改的时候要先删除原先的购物车数据  再更新（skuId为0代表的是加入购物车）
        if (!Long.valueOf(CommonConstants.NUMBER_ZERO).equals(skuId) && !apiShoppingCartDto.getSkuId().equals(skuId)) {
            //删除缓存里面的原商品
            shoppingCartRedisKey.hdel(userKey, JSON.toJSONString(skuId));
        }
        shoppingCartRedisKey.hset(userKey, JSON.toJSONString(apiShoppingCartDto.getSkuId()), JSON.toJSONString(apiShoppingCartDto));
    }

    /**
     * 处理消息队列更新购物车数据库数据
     *
     * @param shoppingCartMessage
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateShoppingCartDatabase(ShoppingCartMessage shoppingCartMessage) {
        ShoppingCart addShoppingCart = shoppingCartMessage.getApiShoppingCartDto().coverShoppingCart();
        addShoppingCart.setTenantId(shoppingCartMessage.getTenantId());
        addShoppingCart.setShopId(shoppingCartMessage.getShopId());
        String userId = shoppingCartMessage.getUserId();
        Long skuId = shoppingCartMessage.getSkuId();
        //等于0说明是加入购物车，不等于0说明是修改购物车数据
        if (!Long.valueOf(CommonConstants.NUMBER_ZERO).equals(skuId)) {
            this.baseMapper.delete(new QueryWrapper<ShoppingCart>().eq("user_id", userId).eq("sku_id", skuId));
        }
        //新增购物车数据
        ShoppingCart shoppingCart = this.baseMapper.selectOne(new QueryWrapper<ShoppingCart>().eq("user_id", addShoppingCart.getUserId()).eq("sku_id", addShoppingCart.getSkuId()));
        if (!BeanUtil.isEmpty(shoppingCart)) {
            //判断新老数据sku是否一致（区分修改是否更换sku）
            int quantity = addShoppingCart.getGoodsNumber();
            if (!addShoppingCart.getSkuId().equals(skuId)) {
                //如果存在相同规格的商品，查询出来的商品数量累加
                quantity = quantity + shoppingCart.getGoodsNumber();
            }
            //如果存在相同规格的商品，查询出来的商品数量累加
            shoppingCart.setGoodsNumber(quantity);
            int update = this.baseMapper.updateById(shoppingCart);
            if (update == 0) {
                throw new ServiceException("更新失败！", SystemCode.DATA_UPDATE_FAILED.getCode());
            }
        } else {
            //不存在相同规格的商品，数据库插入一条商品数据
            int insert = this.baseMapper.insert(addShoppingCart);
            if (insert == 0) {
                throw new ServiceException("新增失败！", SystemCode.DATA_ADD_FAILED.getCode());
            }
        }
    }

    /**
     * 批量删除购物车信息
     *
     * @param apiShoppingCartDtos
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteShoppingCartList(List<ApiShoppingCartDto> apiShoppingCartDtos) {
        CurUserDto curUserDto = CurUserUtil.getHttpCurUser();
        String userId = curUserDto.getUserId();
        ShoppingCartRedisKey shoppingCartRedisKey = new ShoppingCartRedisKey();
        String shopId = ShopContextHolder.getShopId();
        String tenantId = TenantContextHolder.getTenantId();
        //用户购物车缓存key值 用户id+租户id+商户id
        String userKey = userId + tenantId + shopId;
        apiShoppingCartDtos.forEach(bean -> {
            bean.setUserId(userId);
            //清除缓存购物车商品数据
            shoppingCartRedisKey.hdel(userKey, JSON.toJSONString(bean.getSkuId()));
            //删除数据库数据
            this.baseMapper.delete(new QueryWrapper<ShoppingCart>().eq("user_id", bean.getUserId()).eq("sku_id", bean.getSkuId()));
        });
    }

    /**
     * 订单结算删除结算的购物车商品数据
     *
     * @param skuIds
     * @param userId
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteShoppingCartByOrder(List<Long> skuIds, String userId) {
        ShoppingCartRedisKey shoppingCartRedisKey = new ShoppingCartRedisKey();
        String shopId = ShopContextHolder.getShopId();
        String tenantId = TenantContextHolder.getTenantId();
        //用户购物车缓存key值 用户id+租户id+商户id
        String userKey = userId + tenantId + shopId;
        if (CollectionUtil.isNotEmpty(skuIds)) {
            skuIds.forEach(skuId -> {
                //清除缓存购物车商品数据
                shoppingCartRedisKey.hdel(userKey, JSON.toJSONString(skuId));
                //删除数据库数据
                this.baseMapper.delete(new QueryWrapper<ShoppingCart>().eq("user_id", userId).eq("sku_id", skuId));
            });
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据用户id获取购物车商品信息
     *
     * @param userId 用户id
     * @param tenantId
     * @param shopId
     * @return 购物车商品list
     */
    private List<ApiShoppingCartVo> getData(String userId, String tenantId, String shopId) {
        ShoppingCartRedisKey shoppingCartRedisKey = new ShoppingCartRedisKey();
        GoodsProductRedisKey goodsProductRedisKey = new GoodsProductRedisKey();
        GoodsSkuStockRedisKey goodsSkuStockRedisKey = new GoodsSkuStockRedisKey();
        //获取当前线程中的租户id的店铺信息;getTemplateCodeEnum 获取当前店铺使用模板类型
        CurShopInfoDto tenantIdShopInfo = CurUserUtil.getTenantIdShopInfo();
        SaleMode saleMode = new SaleMode();
        final Long saleModeId = saleMode.getId();
        //用户购物车缓存key值 用户id+租户id+商户id
        String userKey = userId + tenantId + shopId;
        //判断缓存key是否存在
        List<String> stringList = shoppingCartRedisKey.hvals(userKey);
        if (CollectionUtil.isNotEmpty(stringList)) {
            //存在：获取缓存数据
            List<ApiShoppingCartVo> apiShoppingCartVos = JSON.parseArray(String.valueOf(stringList), ApiShoppingCartVo.class);
            List<ApiShoppingCartVo> apiShoppingCartVoList = new ArrayList<>(apiShoppingCartVos.size());
            //取缓存商品基础信息（包括商品状态）
            if (CollectionUtil.isNotEmpty(apiShoppingCartVos)) {
                apiShoppingCartVos.forEach(apiShoppingCartVo -> {
                    String productId = String.valueOf(apiShoppingCartVo.getProductId());
                    ApiShoppingCartProductVo apiShoppingCartProductVo = JSON.parseObject(goodsProductRedisKey.get(productId), ApiShoppingCartProductVo.class);
                    BeanUtil.copyProperties(apiShoppingCartProductVo, apiShoppingCartVo);
                    //商品sku库存赋值
                    if(CollectionUtil.isNotEmpty(apiShoppingCartVo.getSkuStocks())){
                        apiShoppingCartVo.getSkuStocks().forEach(skuStockVo -> {
                            String stock = goodsSkuStockRedisKey.get(String.valueOf(skuStockVo.getId()));
                            if(StringUtil.isNotEmpty(stock)){
                                skuStockVo.setStock(Integer.valueOf(stock));
                            }else{
                                skuStockVo.setStock(null);
                            }
                        });
                    }
                    apiShoppingCartVoList.add(apiShoppingCartVo);
                });
            }
            //按购物车车商品的更新时间倒序显示
            SortUtil.sortByMethod(apiShoppingCartVoList, "getSortTime", true);
            return apiShoppingCartVoList;
        }
        return null;
    }
}
