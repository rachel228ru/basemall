package com.medusa.gruul.shops.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medusa.gruul.common.data.annotation.EscapeShop;
import com.medusa.gruul.shops.api.entity.ShopsRenovationAssembly;
import com.medusa.gruul.shops.model.param.ShopsRenovationAssemblyParam;
import com.medusa.gruul.shops.model.param.ShopsRenovationPageParam;
import com.medusa.gruul.shops.model.vo.ShopsRenovationAssemblyVo;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author create by zq
 * @date created in 2019/11/15
 */
@Repository
public interface ShopsRenovationTemPageAssMapper extends BaseMapper<ShopsRenovationAssembly> {


    /**
     * 店铺装修模板 - 页面插件属性配置查询list
     *
     * @param param
     * @return Result
     */
    List<ShopsRenovationAssemblyVo> listTemplatePageAssembly(ShopsRenovationAssemblyParam param);


    /**
     * 店铺装修模板 - 页面插件属性配置查询list by page id
     *
     * @param pageId
     * @return List
     */
    @EscapeShop
    List<ShopsRenovationAssembly> listTemplatePageAssemblyByPageId(Long pageId);

    /**
     * 获取当前装修页面的商品专区
     * @param param
     * @return
     */
    List<ShopsRenovationAssembly> getFitmentInfoByTypeNotNull(ShopsRenovationPageParam param);
}
