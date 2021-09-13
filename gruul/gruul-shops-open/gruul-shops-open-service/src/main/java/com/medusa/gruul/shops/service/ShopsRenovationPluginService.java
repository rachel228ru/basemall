package com.medusa.gruul.shops.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.shops.api.entity.ShopsRenovationPlugin;
import com.medusa.gruul.shops.model.param.ShopsRenovationPluginParam;

import java.util.List;


/**
 * @author create by zq
 * @date created in 2020/01/14
 */
public interface ShopsRenovationPluginService extends IService<ShopsRenovationPlugin> {


    /**
     * 店铺装修模板 - 全局控件属性配置新增&修改
     *
     * @param param
     * @return Result
     */
    Result addPlugin(ShopsRenovationPluginParam param);


    /**
     * 店铺装修模板 - 全局控件属性配置逻辑删除 by ids
     *
     * @param ids
     * @return Result
     */
    Result delPlugin(String ids);


    /**
     * 店铺装修模板 - 全局控件属性配置查询list
     *
     * @param param
     * @return Result
     */
    Result listPlugin(ShopsRenovationPluginParam param);


    /**
     * 店铺装修模板 - 批量全局控件属性配置新增&修改
     *
     * @param param
     * @return Result
     */
    Result addPlugins(List<ShopsRenovationPluginParam> param);
}
