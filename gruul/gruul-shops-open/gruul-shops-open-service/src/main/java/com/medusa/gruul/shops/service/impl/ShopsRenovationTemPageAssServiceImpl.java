package com.medusa.gruul.shops.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.util.Md5Utils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.common.core.util.SystemCode;
import com.medusa.gruul.common.data.annotation.EscapeShop;
import com.medusa.gruul.shops.api.entity.ShopsRenovationAssembly;
import com.medusa.gruul.shops.api.entity.ShopsRenovationPage;
import com.medusa.gruul.shops.api.entity.ShopsRenovationTemplate;
import com.medusa.gruul.shops.mapper.ShopsRenovationTemPageAssMapper;
import com.medusa.gruul.shops.mapper.ShopsRenovationTemPageMapper;
import com.medusa.gruul.shops.model.param.ShopsRenovationAssemblyParam;
import com.medusa.gruul.shops.model.vo.ShopsRenovationAssemblyVo;
import com.medusa.gruul.shops.properties.GlobalConstant;
import com.medusa.gruul.shops.properties.ShopsRenovationRedisTools;
import com.medusa.gruul.shops.service.ShopsRenovationTemPageAssService;
import com.medusa.gruul.shops.service.ShopsRenovationTemPageService;
import com.medusa.gruul.shops.service.ShopsRenovationTemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;


/**
 * @author create by zq
 * @date created in 2020/01/14
 */
@Service(value = "shopsRenovationTemPageAssServiceImpl")
public class ShopsRenovationTemPageAssServiceImpl extends ServiceImpl<ShopsRenovationTemPageAssMapper, ShopsRenovationAssembly> implements ShopsRenovationTemPageAssService {
    @Autowired
    private ShopsRenovationTemPageMapper shopsRenovationTemPage;

    @Autowired
    ShopsRenovationTemPageService shopsRenovationTemPageService;

    @Autowired
    ShopsRenovationTemService shopsRenovationTemService;

    @Autowired
    ShopsRenovationTemPageAssMapper shopsRenovationTemPageAssMapper;

    /**
     * addTemplatePageAssembly
     *
     * @param params
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @EscapeShop
    public Result addTemplatePageAssembly(List<ShopsRenovationAssemblyParam> params) {

        Long pageId = params.get(0).getPageId();
        if (params.get(0).getModelId()!=null){
            //根据modelId获取pageId
            pageId = getPageId(params);
        }
        new LambdaUpdateChainWrapper<>(this.baseMapper)
                .eq(ShopsRenovationAssembly::getPageId, pageId)
                .set(ShopsRenovationAssembly::getDeleted, GlobalConstant.STRING_ONE)
                .update();

        params.stream().forEach(entity -> {
            ShopsRenovationAssembly assembly = new ShopsRenovationAssembly();
            if (null == entity.getId()) {
                if (params.get(0).getModelId()!=null){
                    entity.setPageId(getPageId(params));
                }

                BeanUtil.copyProperties(entity, assembly);
                if (!this.save(assembly)) {
                    throw new ServiceException(SystemCode.DATA_ADD_FAILED.getMsg());
                }
            }
            ShopsRenovationAssembly shopsRenovationAssembly = new ShopsRenovationAssembly();
            BeanUtil.copyProperties(entity, shopsRenovationAssembly);
            if (this.updateById(shopsRenovationAssembly)) {
                throw new ServiceException(SystemCode.DATA_UPDATE_FAILED.getMsg());
            }
        });
        ShopsRenovationRedisTools redisTools = new ShopsRenovationRedisTools();
        redisTools.innerRemoveCache(GlobalConstant.STRING_SHOP_TEMPLATE_PAGE_ASSEMBLY);
        redisTools.innerRemoveCache(GlobalConstant.STRING_SHOP_TEMPLATE_KEY);
        return Result.ok();
    }


    /**
     * 根据modeId获取pageId
     *
     * @param params
     * @return long
     */
    private Long getPageId(List<ShopsRenovationAssemblyParam> params) {
        String modelId = params.get(0).getModelId();
        log.warn(modelId+"----------------------------------");
        ShopsRenovationPage shopsRenovationPage = shopsRenovationTemPage.selectByModelId(modelId);
        return  shopsRenovationPage.getId();
    }


    /**
     * 店铺装修模板 - 页面插件属性配置逻辑删除 by ids
     *
     * @param ids
     * @return Result
     */
    @Override
    @EscapeShop
    public Result delTemplatePageAssembly(String ids) {
        if (StringUtils.isBlank(ids)) {
            throw new ServiceException(SystemCode.PARAM_MISS.getMsg());
        }
        Arrays.asList(ids.split(GlobalConstant.STRING_COMMA)).stream().forEach(id -> {
            boolean update = new LambdaUpdateChainWrapper<>(this.baseMapper)
                    .eq(ShopsRenovationAssembly::getId, id)
                    .set(ShopsRenovationAssembly::getDeleted, GlobalConstant.STRING_ONE)
                    .update();
            if (!update) {
                throw new ServiceException("delete page assembly fail!", SystemCode.DATA_UPDATE_FAILED_CODE);
            }
        });

        ShopsRenovationRedisTools redisTools = new ShopsRenovationRedisTools();
        redisTools.innerRemoveCache(GlobalConstant.STRING_SHOP_PAGE_PLUGIN);
        redisTools.innerRemoveCache(GlobalConstant.STRING_SHOP_TEMPLATE_KEY);
        return Result.ok();
    }


    /**
     * 店铺装修模板 - 页面插件属性配置查询list
     *
     * @param param
     * @return Result
     */
    @Override
    @EscapeShop
    public Result listTemplatePageAssembly(ShopsRenovationAssemblyParam param) {
        /** 临时移除缓存查询 */
        if (null == param.getModelId()){
            return Result.ok(this.baseMapper.listTemplatePageAssembly(param));
        }
        ShopsRenovationPage shopsRenovationPage = shopsRenovationTemPage.selectByModelId(param.getModelId());
        if (BeanUtil.isEmpty(shopsRenovationPage)){
            return Result.ok("无此专区商品信息");
        }
        Long id = shopsRenovationPage.getId();
        param.setPageId(id);
        return Result.ok(this.baseMapper.listTemplatePageAssembly(param));
    }


    /**
     * 店铺装修模板 - 页面插件属性配置查询list by page id
     *
     * @param pageId
     * @return List
     */
    @Override
    @EscapeShop
    public List<ShopsRenovationAssembly> listTemplatePageAssemblyByPageId(Long pageId) {
        return this.baseMapper.listTemplatePageAssemblyByPageId(pageId);
    }


    @Override
    public boolean updateSpecialArea(String tenantId,String shopId, String linkName,String newLinkName) {
        ShopsRenovationTemplate shopsRenovationTemplate = shopsRenovationTemService.getOne(new QueryWrapper<ShopsRenovationTemplate>()
                .eq("online_status", CommonConstants.NUMBER_ONE)
                .eq("shop_id", shopId)
                .eq("tenant_id", tenantId).last("LIMIT 1"));
        if (shopsRenovationTemplate == null){
            return true;
        }
        ShopsRenovationPage shopsRenovationPage = shopsRenovationTemPageService.getOne(new QueryWrapper<ShopsRenovationPage>()
                .eq("template_id", shopsRenovationTemplate.getId()).eq("is_def", CommonConstants.NUMBER_ONE).last("LIMIT 1"));
        if (shopsRenovationPage == null){
            return true;
        }
        Long pageId = shopsRenovationPage.getId();
        List<ShopsRenovationAssembly> shopsRenovationAssemblies = shopsRenovationTemPageAssMapper.listTemplatePageAssemblyByPageId(pageId);
        if (CollectionUtil.isEmpty(shopsRenovationAssemblies)){
            return true;
        }
        String properties = null;
        for (int i = 0; i < shopsRenovationAssemblies.size(); i++) {
            properties = shopsRenovationAssemblies.get(i).getProperties().replaceAll("\""+linkName+"\"","\""+ newLinkName+"\"");
            shopsRenovationAssemblies.get(i).setProperties(properties);
        }
        return this.updateBatchById(shopsRenovationAssemblies);
    }

}
