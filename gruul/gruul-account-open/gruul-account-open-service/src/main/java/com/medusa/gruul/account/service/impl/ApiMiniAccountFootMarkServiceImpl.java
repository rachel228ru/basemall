package com.medusa.gruul.account.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.account.api.constant.AccountStatusController;
import com.medusa.gruul.account.api.entity.MiniAccountFootMark;
import com.medusa.gruul.account.mapper.MiniAccountFootMarkMapper;
import com.medusa.gruul.account.model.dto.AccountFootMarkDto;
import com.medusa.gruul.account.model.vo.AccountFootMarkParam;
import com.medusa.gruul.account.model.vo.AccountFootMarkVo;
import com.medusa.gruul.account.service.IApiMiniAccountFootMarkService;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.CurUserUtil;
import com.medusa.gruul.common.core.util.SystemCode;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.common.dto.CurUserDto;
import com.medusa.gruul.goods.api.constant.GoodsProductRedisKey;
import com.medusa.gruul.goods.api.model.dto.manager.SkuStockDto;
import com.medusa.gruul.goods.api.model.vo.api.ApiShoppingCartProductVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author  xiaoq
 *
 * @data 2020/2/28 10:39
 */
@Service
public class ApiMiniAccountFootMarkServiceImpl extends ServiceImpl<MiniAccountFootMarkMapper, MiniAccountFootMark>
        implements IApiMiniAccountFootMarkService {

    @Resource
    private MiniAccountFootMarkMapper accountFootMarkMapper;

    /**
     * @param accountFootMarkParam 分页对象
     * @return
     */
    @Override
    public List<String> getAccountFootMark(AccountFootMarkParam accountFootMarkParam) {
        CurUserDto curUserDto = CurUserUtil.getHttpCurUser();
        String userId = curUserDto.getUserId();
        String shopId = ShopContextHolder.getShopId();
        String tenantId = TenantContextHolder.getTenantId();
        List<String> accountFootMarkInfo = getAccountFootMarkInfo(accountFootMarkParam, userId, tenantId, shopId);
        return accountFootMarkInfo;
    }

    private List<String> getAccountFootMarkInfo(AccountFootMarkParam accountFootMarkParam, String userId,
                                                String tenantId, String shopId) {
        // 获取商品缓存数据 key
        GoodsProductRedisKey goodsProductRedisKey = new GoodsProductRedisKey();
        IPage<AccountFootMarkVo> accountFootMarkVo = new Page<>(accountFootMarkParam.getCurrent(),
                accountFootMarkParam.getSize());
        accountFootMarkParam.setUserId(userId);
        // 获取分页数据后的 用户足迹信息
        List<AccountFootMarkVo> accountFootMarks = accountFootMarkMapper
                .selectAccountFootMarkList(accountFootMarkVo, accountFootMarkParam);
        List<LocalDateTime> collect = accountFootMarks.stream().map(AccountFootMarkVo::getCreateTime).distinct()
                .collect(Collectors.toList());
        String getTime = "";
        List<String> list = new ArrayList<>(11);
        for (LocalDateTime localDateTime : collect) {
            String time = localDateTime.format(DateTimeFormatter.ofPattern("MM-dd"));
            List<AccountFootMarkVo> temp = new ArrayList<>();
            for (AccountFootMarkVo footMarkVo : accountFootMarks) {
                LocalDateTime createTime = footMarkVo.getCreateTime();
                getTime = createTime.format(DateTimeFormatter.ofPattern("MM-dd"));
                if (footMarkVo.getCreateTime().equals(localDateTime)) {
                    String productId = String.valueOf(footMarkVo.getProductId());
                    ApiShoppingCartProductVo apiShoppingCartProductVo = JSON
                            .parseObject(goodsProductRedisKey.get(productId), ApiShoppingCartProductVo.class);
                    if (ObjectUtil.isEmpty(apiShoppingCartProductVo)){
                        continue;
                    }
                    BeanUtil.copyProperties(apiShoppingCartProductVo, footMarkVo);
                    footMarkVo.setProductPrice(apiShoppingCartProductVo.getSkuStocks().get(0).getPrice());
                    footMarkVo.setOriginalPrice(apiShoppingCartProductVo.getSkuStocks().get(0).getOriginalPrice());
                    footMarkVo.setTime(getTime);
                    footMarkVo.setStatus(AccountStatusController.STATUS);
                    //遍历商品规格信息 得到该商品的 规格剩余数量
                    List<SkuStockDto> skuStocks = apiShoppingCartProductVo.getSkuStocks();
                    for (SkuStockDto skuStock : skuStocks) {
                        Integer stock = skuStock.getStock();
                        if (stock != 0) {
                            footMarkVo.setStatus(apiShoppingCartProductVo.getStatus());
                            break;
                        }
                    }
                    if (apiShoppingCartProductVo.getDeleted() == 1) {
                        footMarkVo.setStatus(1);
                    }
                    temp.add(footMarkVo);
                }
            }
            list.add(JSON.toJSONString(temp));


        }
        return list;
    }

    /**
     * 添加用户足迹
     *
     * @param accountFootMarkDto 商品信息
     */
    @Override
    public void addAccountFootMark(AccountFootMarkDto accountFootMarkDto) {
        CurUserDto curUserDto = CurUserUtil.getHttpCurUser();
        if (curUserDto == null) {
            throw new ServiceException("数据异常");
        }
        System.out.println(accountFootMarkDto.toString());
        String userId = curUserDto.getUserId();
        System.out.println(userId);
        String productId = accountFootMarkDto.getProductId();
        MiniAccountFootMark miniAccountFootMark = accountFootMarkMapper.selectByProductId(productId, userId);
        // 查看用户今日是否浏览过该商品
        accountFootMarkDto.setUserId(userId);
        if (miniAccountFootMark == null) {
            // 用户今日未添加过该足迹
            MiniAccountFootMark accountFootMark = accountFootMarkDto.coverMiniAccountFootMark();
            int insert = accountFootMarkMapper.insert(accountFootMark);
            if (insert < 1) {
                throw new ServiceException("添加用户足迹失败", SystemCode.DATA_ADD_FAILED_CODE);
            }
            int i = accountFootMarkMapper.selectAccountFootMarkCount(userId);
            if (i > AccountStatusController.FOOT_NUM) {
                //保留该用户最新的100条足迹数据
                int delCount = i - AccountStatusController.FOOT_NUM;
                int updateBeyondFootMarkCount = accountFootMarkMapper.updateBeyondFootMark(userId, delCount);
                if (updateBeyondFootMarkCount < 1) {
                    throw new ServiceException("更新用户足迹状态失败", SystemCode.DATA_ADD_FAILED_CODE);
                }
            }
        } else {
            // 用户今日添加了该足迹
            miniAccountFootMark.setCreateTime(LocalDateTime.now());
            int update = accountFootMarkMapper.updateById(miniAccountFootMark);
            if (update < 1) {
                throw new ServiceException("更新用户足迹失败", SystemCode.DATA_UPDATE_FAILED_CODE);
            }
        }
    }

    /**
     * 删除用户足迹数据
     *
     * @param longs 用户足迹id
     * @param type  :0-删除, 1-清空
     */
    @Override
    public void delAccountFootMark(List<Long> longs, Integer type) {
        // 根据选择的的用户足迹ids进行删除
        if (type.equals(0)) {
            List<MiniAccountFootMark> miniAccountFootMarks = baseMapper.selectBatchIds(longs);
            for (MiniAccountFootMark miniAccountFootMark : miniAccountFootMarks) {
                //
                int i = accountFootMarkMapper.deleteById(miniAccountFootMark.getFootmarkId());
                if (i < 1) {
                    throw new ServiceException("删除用户足迹失败", SystemCode.DATA_UPDATE_FAILED_CODE);
                }
            }
        }
        // 根据用户id 进行删除
        if (type.equals(1)) {
            CurUserDto curUserDto = CurUserUtil.getHttpCurUser();
            if (curUserDto == null) {
                throw new ServiceException("数据异常");
            }
            String userId = curUserDto.getUserId();
            int i = accountFootMarkMapper.deleteByUserId(userId);
            if (i < 1) {
                throw new ServiceException("清空用户足迹失败", SystemCode.DATA_UPDATE_FAILED_CODE);
            }
        }
    }

    @Override
    public int getAccountFootMarkCount() {
        CurUserDto curUserDto = CurUserUtil.getHttpCurUser();
        if (curUserDto == null) {
            throw new ServiceException("数据异常");
        }
        String userId = curUserDto.getUserId();
        int i = accountFootMarkMapper.selectAccountFootMarkCount(userId);

        return i;
    }
}
