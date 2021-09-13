package com.medusa.gruul.shops.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.medusa.gruul.shops.api.entity.ShopsRenovationPage;
import com.medusa.gruul.shops.api.entity.ShopsRenovationTemplate;
import com.medusa.gruul.shops.model.param.ShopsRenovationPageParam;
import com.medusa.gruul.shops.model.vo.ShopsRenovationPageVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author create by zq
 * @date created in 2019/11/15
 */
@Repository
public interface ShopsRenovationTemPageMapper extends BaseMapper<ShopsRenovationPage> {


    /**
     * 获取商铺装修模板页面list
     *
     * @param page
     * @param param
     * @return Result
     */
    List listTemplatePage(@Param("page") IPage<ShopsRenovationPageVo> page, @Param("param") ShopsRenovationPageParam param);

    /**
     * 根据modelId获取 id 及page_id
     * @param modelId modelId
     * @return
     */
    ShopsRenovationPage selectByModelId(@Param("modelId") String modelId);

    /**
     * 获取当前商品组件页面所有专区
     *
     * @param page
     * @param param
     * @return
     */
    List<ShopsRenovationPageVo> getFitmentInfoByTypeNotNull(@Param("page") IPage<ShopsRenovationPageVo> page, @Param("param") ShopsRenovationPageParam param);
}
