package com.medusa.gruul.shops.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.shops.api.entity.ShopsRenovationTemplate;
import com.medusa.gruul.shops.api.model.RenovationTemplateDto;
import com.medusa.gruul.shops.model.param.ShopsRenovationTemplateParam;


/**
 * @author create by zq
 * @date created in 2020/01/14
 */
public interface ShopsRenovationTemService extends IService<ShopsRenovationTemplate> {


    /**
     * 新增商铺装修模板
     *
     * @param param
     * @return Result
     */
    Result addTemplate(ShopsRenovationTemplateParam param);


    /**
     * 删除商铺装修模板 by ids
     *
     * @param ids
     * @return Result
     */
    Result delTemplate(String ids);


    /**
     * 获取商铺装修模板list
     *
     * @param param
     * @return Result
     */
    Result listTemplate(ShopsRenovationTemplateParam param);


    /**
     * 删除默认装修模板 by ids
     *
     * @param ids
     * @return Result
     */
    Result delDefTemplate(String ids);


    /**
     * 获取默认装修模板list
     *
     * @param param
     * @return Result
     */
    Result listDefTemplate(ShopsRenovationTemplateParam param);


    /**
     * 复制模板 by id
     *
     * @param id
     * @return Result
     */
    Result copyTemplateById(Long id);

    /**
     * 初始化模板数据
     *
     * @param templateSettingDto
     * @return void
     * @author alan
     * @date 2020/4/23 21:18
     */
    void init(RenovationTemplateDto templateSettingDto);


    /**
     * 小程序装修默认聚合接口
     *
     * @return Result
     */
    Result templateDefOne();

}
