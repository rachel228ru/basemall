package com.medusa.gruul.shops.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.shops.api.entity.ShopsRenovationAssembly;
import com.medusa.gruul.shops.model.param.ShopsRenovationAssemblyParam;
import com.medusa.gruul.shops.model.param.ShopsRenovationPageParam;

import java.util.List;


/**
 * @author create by zq
 * @date created in 2020/01/14
 */
public interface ShopsRenovationTemPageAssService extends IService<ShopsRenovationAssembly> {


    /**
     * 店铺装修模板 - 页面插件属性配置新增&修改
     *
     * @param params
     * @return Result
     */
    Result addTemplatePageAssembly(List<ShopsRenovationAssemblyParam> params);


    /**
     * 店铺装修模板 - 页面插件属性配置逻辑删除 by ids
     *
     * @param ids
     * @return Result
     */
    Result delTemplatePageAssembly(String ids);


    /**
     * 店铺装修模板 - 页面插件属性配置查询list
     *
     * @param param
     * @return Result
     */
    Result listTemplatePageAssembly(ShopsRenovationAssemblyParam param);


    /**
     * 店铺装修模板 - 页面插件属性配置查询list by page id
     *
     * @param pageId
     * @return List
     */
    List<ShopsRenovationAssembly> listTemplatePageAssemblyByPageId(Long pageId);

    /**
     * 修改专区名称
     * @param tenantId  租户id
     * @param shopId   店铺id
     * @param linkName  专区原名称
     * @param newLinkName 专区新名称
     * @return
     */
    boolean updateSpecialArea(String tenantId,String shopId,String linkName,String newLinkName);
}
