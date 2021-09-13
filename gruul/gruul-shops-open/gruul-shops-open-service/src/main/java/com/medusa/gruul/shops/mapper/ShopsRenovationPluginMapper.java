package com.medusa.gruul.shops.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medusa.gruul.shops.api.entity.ShopsRenovationPlugin;
import com.medusa.gruul.shops.model.param.ShopsRenovationPluginParam;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author create by zq
 * @date created in 2019/11/15
 */
@Repository
public interface ShopsRenovationPluginMapper extends BaseMapper<ShopsRenovationPlugin> {


    /**
     * 店铺装修模板 - 全局控件属性配置查询list
     *
     * @param param
     * @return Result
     */
    List listPlugin(ShopsRenovationPluginParam param);


}
