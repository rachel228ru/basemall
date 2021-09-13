package com.medusa.gruul.shops.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.common.core.util.SystemCode;
import com.medusa.gruul.common.data.annotation.EscapeShop;
import com.medusa.gruul.shops.api.entity.ShopsRenovationPlugin;
import com.medusa.gruul.shops.mapper.ShopsRenovationPluginMapper;
import com.medusa.gruul.shops.model.param.ShopsRenovationPluginParam;
import com.medusa.gruul.shops.properties.GlobalConstant;
import com.medusa.gruul.shops.properties.ShopsRenovationRedisTools;
import com.medusa.gruul.shops.service.ShopsRenovationPluginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;


/**
 * @author create by zq
 * @date created in 2020/01/14
 */
@Service(value = "shopsRenovationPluginServiceImpl")
public class ShopsRenovationPluginServiceImpl extends ServiceImpl<ShopsRenovationPluginMapper, ShopsRenovationPlugin> implements ShopsRenovationPluginService {


    /**
     * 店铺装修模板 - 全局控件属性配置新增&修改
     *
     * @param param
     * @return Result
     */
    @Override
    @EscapeShop
    public Result addPlugin(ShopsRenovationPluginParam param) {
        if (param.getId() == null) {
            throw new ServiceException("控件id不能为空!");
        }

        ShopsRenovationPlugin shopsRenovationPlugin = new ShopsRenovationPlugin();
        BeanUtil.copyProperties(param, shopsRenovationPlugin);
        if (this.updateById(shopsRenovationPlugin)) {
            ShopsRenovationRedisTools redisTools = new ShopsRenovationRedisTools();
            redisTools.innerRemoveCache(GlobalConstant.STRING_SHOP_PAGE_PLUGIN);
            redisTools.innerRemoveCache(GlobalConstant.STRING_SHOP_TEMPLATE_KEY);
            return Result.ok();
        }
        return Result.failed();
    }


    /**
     * 店铺装修模板 - 全局控件属性配置逻辑删除 by ids
     *
     * @param ids
     * @return Result
     */
    @Override
    @EscapeShop
    public Result delPlugin(String ids) {
        if (StringUtils.isBlank(ids)) {
            throw new ServiceException(SystemCode.PARAM_MISS.getMsg());
        }
        Arrays.asList(ids.split(GlobalConstant.STRING_COMMA)).stream().forEach(id -> {
            boolean update = new LambdaUpdateChainWrapper<>(this.baseMapper)
                    .eq(ShopsRenovationPlugin::getId, id)
                    .set(ShopsRenovationPlugin::getDeleted, GlobalConstant.STRING_ONE)
                    .update();
            if (!update) {
                throw new ServiceException("delete plugin fail!", SystemCode.DATA_UPDATE_FAILED_CODE);
            }
        });

        ShopsRenovationRedisTools redisTools = new ShopsRenovationRedisTools();
        redisTools.innerRemoveCache(GlobalConstant.STRING_SHOP_PAGE_PLUGIN);
        redisTools.innerRemoveCache(GlobalConstant.STRING_SHOP_TEMPLATE_KEY);
        return Result.ok();
    }


    /**
     * 店铺装修模板 - 全局控件属性配置查询list
     *
     * @param param
     * @return Result
     */
    @Override
    @EscapeShop
    public Result listPlugin(ShopsRenovationPluginParam param) {
        /** 临时移除缓存查询 */
        return Result.ok(this.baseMapper.listPlugin(param));
    }


    /**
     * 店铺装修模板 - 批量全局控件属性配置新增&修改
     *
     * @param list
     * @return Result
     */
    @Override
    @EscapeShop
    @Transactional(rollbackFor = Exception.class)
    public Result addPlugins(List<ShopsRenovationPluginParam> list) {
        int size = list.size();
        ShopsRenovationRedisTools redisTools = new ShopsRenovationRedisTools();
        for (int i = 0; i < list.size(); i++) {
            ShopsRenovationPluginParam param = list.get(i);
            if (param.getId() == null) {
                ShopsRenovationPlugin shopsRenovationPlugin = new ShopsRenovationPlugin();
                BeanUtil.copyProperties(param, shopsRenovationPlugin);
                if (this.save(shopsRenovationPlugin)) {
                    size--;
                    redisTools.innerRemoveCache(GlobalConstant.STRING_SHOP_PAGE_PLUGIN);
                }
            }
            ShopsRenovationPlugin shopsRenovationPlugin = new ShopsRenovationPlugin();
            BeanUtil.copyProperties(param, shopsRenovationPlugin);
            if (this.updateById(shopsRenovationPlugin)) {
                size--;
                redisTools.innerRemoveCache(GlobalConstant.STRING_SHOP_PAGE_PLUGIN);
            }
        }

        if (size != 0) {
            return Result.failed();
        }
        return Result.ok();
    }

}
