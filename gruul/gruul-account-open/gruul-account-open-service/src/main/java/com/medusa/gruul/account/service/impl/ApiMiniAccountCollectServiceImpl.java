package com.medusa.gruul.account.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.account.api.constant.AccountStatusController;
import com.medusa.gruul.account.api.entity.MiniAccountCollect;
import com.medusa.gruul.account.conf.AccountRedis;
import com.medusa.gruul.account.constant.RedisConstant;
import com.medusa.gruul.account.mapper.MiniAccountCollectMapper;
import com.medusa.gruul.account.model.dto.UserCollectDto;
import com.medusa.gruul.account.model.vo.UserCollectVo;
import com.medusa.gruul.account.mq.CollectMessage;
import com.medusa.gruul.account.mq.Sender;
import com.medusa.gruul.account.service.IApiMiniAccountCollectService;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.CurUserUtil;
import com.medusa.gruul.common.core.util.SystemCode;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.common.dto.CurUserDto;
import com.medusa.gruul.goods.api.constant.GoodsProductRedisKey;
import com.medusa.gruul.goods.api.model.dto.manager.SkuStockDto;
import com.medusa.gruul.goods.api.model.vo.api.ApiShoppingCartProductVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author  xiaoq
 * 小程序用戶收藏 服務類
 *
 * @data 2020/2/22 15:44
 */
@Service
public class ApiMiniAccountCollectServiceImpl extends ServiceImpl<MiniAccountCollectMapper, MiniAccountCollect>
        implements IApiMiniAccountCollectService {

    @Autowired
    private MiniAccountCollectMapper miniAccountCollectMapper;
    @Autowired
    private Sender sender;

    /**
     * 用戶添加商品信息到用戶收藏 （收藏） 先更新 緩存 後跟新数据表
     *
     * @param userCollectDtos
     */
    @Override
    public void addAccountCollect(List<UserCollectDto> userCollectDtos) {
        for (UserCollectDto userCollectDto : userCollectDtos) {
            CurUserDto curUserDto = CurUserUtil.getHttpCurUser();
            userCollectDto.setUserId(curUserDto.getUserId());
            String shopId = ShopContextHolder.getShopId();
            String tenantId = TenantContextHolder.getTenantId();
            AccountRedis accountRedis = new AccountRedis();
            //缓存中更新 用户收藏商品信息
            updateAccountCollectInfo(userCollectDto, Long.valueOf(CommonConstants.NUMBER_ZERO), accountRedis, tenantId,
                    shopId);
            //消息队列 发送 用户收藏信息
            CollectMessage collectMessage = new CollectMessage();
            collectMessage.setTenantId(tenantId);
            collectMessage.setShopId(shopId);
            collectMessage.setUserId(curUserDto.getUserId());
            collectMessage.setProductId(Long.valueOf(CommonConstants.NUMBER_ZERO));
            collectMessage.setUserCollectDto(userCollectDto);
            sender.sendShoppingCartMessage(collectMessage);
        }
    }


    /**
     * 更新用户收藏数据库数据
     *
     * @param collectMessage mq消息体
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAccountCollect(CollectMessage collectMessage) {
        MiniAccountCollect miniAccountCollect = collectMessage.getUserCollectDto().coverMiniAccountCollect();
        miniAccountCollect.setTenantId(collectMessage.getTenantId());
        miniAccountCollect.setShopId(collectMessage.getShopId());
        String userId = collectMessage.getUserId();
        Long id = collectMessage.getProductId();
        //等于0说明是加入购物车，不等于0说明是修改购物车数据
        if (!Long.valueOf(CommonConstants.NUMBER_ZERO).equals(id)) {
            this.baseMapper.delete(new QueryWrapper<MiniAccountCollect>().eq("user_id", userId).eq("product_id", id));
        }
        //        新增购物车数据
        MiniAccountCollect accountCollect = this.baseMapper.selectOne(
                new QueryWrapper<MiniAccountCollect>().eq("user_id", userId)
                        .eq("product_id", miniAccountCollect.getProductId()));
        if (!BeanUtil.isEmpty(accountCollect)) {
            accountCollect.setUpdateTime(LocalDateTime.now());
            int update = this.baseMapper.updateById(accountCollect);
            if (update == 0) {
                throw new ServiceException("更新失败！", SystemCode.DATA_UPDATE_FAILED.getCode());
            }
        } else {
            int insert = this.baseMapper.insert(miniAccountCollect);
            if (insert < 1) {
                throw new ServiceException("新增失败！", SystemCode.DATA_ADD_FAILED.getCode());
            }
        }
    }

    /**
     * 获取用户的收藏信息
     *
     * @return
     */
    @Override
    public List<UserCollectVo> getUserCollectInfo() {
        CurUserDto curUserDto = CurUserUtil.getHttpCurUser();
        String userId = curUserDto.getUserId();
        String shopId = ShopContextHolder.getShopId();
        String tenantId = TenantContextHolder.getTenantId();
        List<UserCollectVo> collectInfo = getCollectInfo(userId, shopId, tenantId);
        return collectInfo;
    }

    /**
     * 删除用户信息
     *
     * @param userCollectDto 用户收藏信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delAccountCollect(UserCollectDto userCollectDto) {
        CurUserDto curUserDto = CurUserUtil.getHttpCurUser();
        String userId = curUserDto.getUserId();
        String shopId = ShopContextHolder.getShopId();
        String tenantId = TenantContextHolder.getTenantId();
        AccountRedis accountRedis = new AccountRedis();
        String userKey = RedisConstant.COLLECT_KEY.concat(tenantId).concat(":").concat(shopId + ":").concat(userId);
        //删除缓存数据
        accountRedis.hdel(userKey, JSON.toJSONString(userCollectDto.getProductId()));
        //删除数据库书籍
        this.baseMapper.delete(new QueryWrapper<MiniAccountCollect>().eq("user_id", userId)
                .eq("product_id", userCollectDto.getProductId()));

    }


    /**
     * 根据 用户条件信息 获取 用户收藏数据
     *
     * @param userId   用户id
     * @param shopId   商铺id
     * @param tenantId 租户id
     * @return 用户收藏数据
     */
    private List<UserCollectVo> getCollectInfo(String userId, String shopId, String tenantId) {
        //商品缓存key值
        GoodsProductRedisKey goodsProductRedisKey = new GoodsProductRedisKey();
        //用户收藏缓存key值
        AccountRedis accountRedis = new AccountRedis();
        String userKey = RedisConstant.COLLECT_KEY.concat(tenantId).concat(":").concat(shopId + ":").concat(userId);
        List<UserCollectVo> userCollectVos;
        //获取缓存数据 key
        List<String> listKey = accountRedis.hvals(userKey);

        //判断key是否存在
        if (CollectionUtil.isNotEmpty(listKey)) {
            //存在：获取缓存数据
            userCollectVos = JSON.parseArray(String.valueOf(listKey), UserCollectVo.class);
            //取缓存商品基础信息（包括商品状态）
            if (CollectionUtil.isNotEmpty(userCollectVos)) {
                userCollectVos.stream().forEach(userCollectVo -> {
                    String productId = String.valueOf(userCollectVo.getProductId());
                    ApiShoppingCartProductVo apiShoppingCartProductVo = JSON
                            .parseObject(goodsProductRedisKey.get(productId), ApiShoppingCartProductVo.class);
                    BeanUtil.copyProperties(apiShoppingCartProductVo, userCollectVo);
                    userCollectVo.setProductPic(apiShoppingCartProductVo.getPic());
                    userCollectVo.setProductPrice(apiShoppingCartProductVo.getSkuStocks().get(0).getPrice());
                    userCollectVo.setOriginalPrice(apiShoppingCartProductVo.getSkuStocks().get(0).getOriginalPrice());
                    userCollectVo.setStatus(AccountStatusController.STATUS);
                    List<SkuStockDto> skuStocks = apiShoppingCartProductVo.getSkuStocks();
                    for (SkuStockDto skuStock : skuStocks) {
                        Integer stock = skuStock.getStock();
                        if (stock != 0) {
                            userCollectVo.setStatus(apiShoppingCartProductVo.getStatus());
                            break;
                        }
                    }
                    if (apiShoppingCartProductVo.getDeleted() == 1) {
                        userCollectVo.setStatus(2);
                    }
                });

            }
        } else {
            userCollectVos = this.baseMapper.getUserCollectInfoById(userId);
            if (CollectionUtil.isNotEmpty(userCollectVos)) {
                //设置缓存数据
                userCollectVos.forEach(bean -> {
                    accountRedis.hset(userId, JSON.toJSONString(bean.getProductId()), JSON.toJSONString(bean));
                });
            }
        }
        return userCollectVos;
    }

    /**
     * 更新缓存中的商品信息
     *
     * @param userCollectDto 用户收藏商品信息
     * @param accountRedis   key
     * @param tenantId       租户id
     * @param shopId         商铺id
     */
    private void updateAccountCollectInfo(UserCollectDto userCollectDto, Long id, AccountRedis accountRedis,
                                          String tenantId, String shopId) {
        //用户收藏的key值   租户id:商铺id+用户id   xxx:xxxx111asass
        String userKey = RedisConstant.COLLECT_KEY.concat(tenantId).concat(":").concat(shopId + ":").concat(userCollectDto.getUserId());
        String hget = accountRedis.hget(userKey, JSON.toJSONString(userCollectDto.getProductId()));
        if (hget != null) {
            accountRedis
                    .hset(userKey, JSON.toJSONString(userCollectDto.getProductId()), JSON.toJSONString(userCollectDto));
            return;
        }

        //判断商品是加入收藏还是 更新收藏商品信息 修改的时候要先删除原先的购物车数据  再更新（Id为0代表的是加入收藏）
        if (!Long.valueOf(CommonConstants.NUMBER_ZERO).equals(id) && !userCollectDto.getProductId().equals(id)) {
            //删除缓存里面的原商品
            accountRedis.hdel(userKey, JSON.toJSONString(id));
        }
        accountRedis.hset(userKey, JSON.toJSONString(userCollectDto.getProductId()), JSON.toJSONString(userCollectDto));
    }


    /**
     * 查看用户是否多该商品进行了收藏
     *
     * @param productId 商品id
     * @return
     */
    @Override
    public Boolean findAccountIsCollect(Long productId) {
        CurUserDto curUserDto = CurUserUtil.getHttpCurUser();
        String userId = curUserDto.getUserId();
        String shopId = ShopContextHolder.getShopId();
        String tenantId = TenantContextHolder.getTenantId();
        AccountRedis accountRedis = new AccountRedis();
        String userKey = RedisConstant.COLLECT_KEY.concat(tenantId).concat(":").concat(shopId + ":").concat(userId);
        UserCollectVo userCollectVo = JSON
                .parseObject(accountRedis.hget(userKey, JSON.toJSONString(productId)), UserCollectVo.class);
        if (userCollectVo == null) {
            //緩存中 不存在 sql中查詢
            UserCollectDto accountCollect = miniAccountCollectMapper.findAccountCollectByProductId(productId, userId);
            if (accountCollect != null) {
                accountRedis.hset(userKey, JSON.toJSONString(accountCollect.getProductId()),
                        JSON.toJSONString(accountCollect));
            }
            return false;
        }
        return true;
    }

    @Override
    public int getCollectCount() {
        CurUserDto curUserDto = CurUserUtil.getHttpCurUser();
        String userId = curUserDto.getUserId();
        String shopId = ShopContextHolder.getShopId();
        String tenantId = TenantContextHolder.getTenantId();
        AccountRedis accountRedis = new AccountRedis();
        String userKey = RedisConstant.COLLECT_KEY.concat(tenantId).concat(":").concat(shopId + ":").concat(userId);
        List<UserCollectVo> userCollectVos;
        //获取缓存数据 key
        List<String> listKey = accountRedis.hvals(userKey);
        //判断key是否存在
        if (CollectionUtil.isNotEmpty(listKey)) {
            return listKey.size();
        } else {
            userCollectVos = this.baseMapper.getUserCollectInfoById(userId);
            if (CollectionUtil.isNotEmpty(userCollectVos)) {
                //设置缓存数据
                userCollectVos.forEach(bean -> {
                    accountRedis.hset(userId, JSON.toJSONString(bean.getProductId()), JSON.toJSONString(bean));
                });
            }
        }
        return userCollectVos.size();
    }
}