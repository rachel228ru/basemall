package com.medusa.gruul.shops.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.shops.api.entity.ShopsMenuConfig;
import com.medusa.gruul.shops.api.entity.ShopsRenovationAssembly;
import com.medusa.gruul.shops.model.param.ShopsMenuConfigParam;
import com.medusa.gruul.shops.model.param.ShopsRenovationAssemblyParam;

import java.util.List;


/**
 * @author create by zq
 * @date created in 2020/01/14
 */
public interface ShopsMenuConfigService extends IService<ShopsMenuConfig> {


    /**
     * 店铺装修模板 - 页面插件属性配置新增&修改
     *
     * @param param
     * @return Result
     */
    Result updateMenuConfig(ShopsMenuConfigParam param);


    /**
     * 获取店铺合伙人菜单属性配置
     *
     * @return Result
     */
    Result getMenuConfig();

}
