package com.medusa.gruul.shops.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medusa.gruul.shops.api.entity.ShopsRenovationTemplate;
import com.medusa.gruul.shops.model.param.ShopsRenovationTemplateParam;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author create by zq
 * @date created in 2019/11/15
 */
@Repository
public interface ShopsRenovationTemMapper extends BaseMapper<ShopsRenovationTemplate> {


    /**
     * 获取商铺装修模板list
     *
     * @param param
     * @return List
     */
    List listTemplate(ShopsRenovationTemplateParam param);


    /**
     * 获取默认装修模板list
     *
     * @param param
     * @return List
     */
    List listDefTemplate(ShopsRenovationTemplateParam param);

}
