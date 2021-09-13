package com.medusa.gruul.shops.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.common.core.util.SystemCode;
import com.medusa.gruul.common.data.annotation.EscapeShop;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.common.dto.CurUserDto;
import com.medusa.gruul.shops.api.entity.ShopsRenovationAssembly;
import com.medusa.gruul.shops.api.entity.ShopsRenovationPage;
import com.medusa.gruul.shops.mapper.ShopsRenovationTemPageAssMapper;
import com.medusa.gruul.shops.mapper.ShopsRenovationTemPageMapper;
import com.medusa.gruul.shops.model.param.ShopsRenovationPageParam;
import com.medusa.gruul.shops.model.vo.ShopsRenovationPageVo;
import com.medusa.gruul.shops.properties.GlobalConstant;
import com.medusa.gruul.shops.properties.ShopsRenovationRedisTools;
import com.medusa.gruul.shops.service.ShopsRenovationTemPageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;


/**
 * @author create by zq
 * @date created in 2020/01/14
 */
@Service(value = "shopsRenovationTemPageServiceImpl")
public class ShopsRenovationTemPageServiceImpl extends ServiceImpl<ShopsRenovationTemPageMapper, ShopsRenovationPage> implements ShopsRenovationTemPageService {
    @Autowired
    private ShopsRenovationTemPageAssMapper shopsRenovationTemPageAssMapper;

    /**
     * 保存模板页面
     *
     * @param param
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result saveTemplatePage(ShopsRenovationPageParam param) {
        ShopsRenovationRedisTools redisTools = new ShopsRenovationRedisTools();
        ShopsRenovationPage renovationPage = new ShopsRenovationPage();
        BeanUtil.copyProperties(param, renovationPage);
        if (null == param.getId()) {
            if (this.save(renovationPage)) {
                redisTools.innerRemoveCache(GlobalConstant.STRING_SHOP_TEMPLATE_PAGE);
                redisTools.innerRemoveCache(GlobalConstant.STRING_SHOP_TEMPLATE_KEY);
                return Result.ok(renovationPage);
            }
            return Result.failed();
        }

        if (GlobalConstant.STRING_ONE.equals(renovationPage.getIsDef())) {
            new LambdaUpdateChainWrapper<>(this.baseMapper)
                    .ne(ShopsRenovationPage::getId, renovationPage.getId())
                    .set(ShopsRenovationPage::getIsDef, GlobalConstant.STRING_ZERO)
                    .update();
        }

        if (this.updateById(renovationPage)) {
            redisTools.innerRemoveCache(GlobalConstant.STRING_SHOP_TEMPLATE_PAGE);
            redisTools.innerRemoveCache(GlobalConstant.STRING_SHOP_TEMPLATE_KEY);
            return Result.ok(renovationPage);
        } else {
            throw new ServiceException(String.format("更新失败, 页面ID: %s !", renovationPage.getId()), SystemCode.DATA_UPDATE_FAILED_CODE);
        }
    }


    /**
     * 删除商铺装修模板页面 by ids or templateId
     *
     * @param ids
     * @param templateId
     * @return Result
     */
    @Override
    @EscapeShop
    public Result delTemplatePage(String ids, String templateId) {
        if (StringUtils.isNotBlank(ids)) {
            Arrays.asList(ids.split(GlobalConstant.STRING_COMMA)).stream().forEach(id -> {
                boolean update = new LambdaUpdateChainWrapper<>(this.baseMapper)
                        .eq(ShopsRenovationPage::getId, id)
                        .set(ShopsRenovationPage::getDeleted, GlobalConstant.STRING_ONE)
                        .update();
                if (!update) {
                    throw new ServiceException("delete fail!", SystemCode.DATA_UPDATE_FAILED_CODE);
                }
            });
        }

        if (StringUtils.isNotBlank(templateId)) {
            boolean update = new LambdaUpdateChainWrapper<>(this.baseMapper)
                    .eq(ShopsRenovationPage::getTemplateId, templateId)
                    .set(ShopsRenovationPage::getDeleted, GlobalConstant.STRING_ONE)
                    .update();
            if (!update) {
                throw new ServiceException("delete fail!", SystemCode.DATA_UPDATE_FAILED_CODE);
            }
        }

        ShopsRenovationRedisTools redisTools = new ShopsRenovationRedisTools();
        redisTools.innerRemoveCache(GlobalConstant.STRING_SHOP_PAGE_PLUGIN);
        redisTools.innerRemoveCache(GlobalConstant.STRING_SHOP_TEMPLATE_KEY);
        return Result.ok();
    }


    /**
     * 获取商铺装修模板页面list
     *
     * @param param
     * @return Result
     */
    @Override
    @EscapeShop
    public Result listTemplatePage(ShopsRenovationPageParam param) {
        IPage<ShopsRenovationPageVo> page = new Page<>(param.getCurrent(), param.getSize());
        return Result.ok(new PageUtils(page.setRecords(this.baseMapper.listTemplatePage(page, param))));
    }

    /**
     * 获取已经完成装修的商品组件
     * @param param
     * @return
     */
    @Override
    public Result fitmentPrefectureEndPage(ShopsRenovationPageParam param) {
        IPage<ShopsRenovationPageVo> page = new Page<>(param.getCurrent(), param.getSize());
        //获取TypeNotNull装修的商品组件信息
        return Result.ok(new PageUtils(page.setRecords(this.baseMapper.getFitmentInfoByTypeNotNull(page, param))));
    }

    /**
     * 删除专区时 匹配删除装修数据
     * @param modelId modelId
     * @return 删除结果
     */
    @Override
    public Boolean delShopRenovationPageByModelId(String modelId)  {
        log.warn(TenantContextHolder.getTenantId()+" : "+ ShopContextHolder.getShopId());
        log.warn("moderId :{}"+modelId);
        ShopsRenovationPage shopsRenovationPage = this.baseMapper.selectByModelId(modelId);
        if (null!=shopsRenovationPage){
            //删除专区
            int i = this.baseMapper.deleteById(shopsRenovationPage.getId());
            if (i < 1){
                return Boolean.FALSE;
            }
            return Boolean.TRUE;
        }
        return Boolean.TRUE;
    }

    /**
     * getByModelId
     *
     * @param modelId
     * @return Result
     */
    @Override
    public Result getByModelId(String modelId) {
        return Result.ok(this.baseMapper.selectList(new QueryWrapper<ShopsRenovationPage>().eq("model_id", modelId)));
    }

}
