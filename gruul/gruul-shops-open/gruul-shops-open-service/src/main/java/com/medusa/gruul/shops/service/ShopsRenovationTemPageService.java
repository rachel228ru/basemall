package com.medusa.gruul.shops.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.shops.api.entity.ShopsRenovationPage;
import com.medusa.gruul.shops.model.param.ShopsRenovationPageParam;
import org.apache.ibatis.annotations.Param;


/**
 * @author create by zq
 * @date created in 2020/01/14
 */
public interface ShopsRenovationTemPageService extends IService<ShopsRenovationPage> {


    /**
     * 保存模板页面
     *
     * @param param
     * @return Result
     */
    Result saveTemplatePage(ShopsRenovationPageParam param);


    /**
     * 删除商铺装修模板页面 by ids or templateId
     *
     * @param ids
     * @param templateId
     * @return Result
     */
    Result delTemplatePage(String ids, String templateId);


    /**
     * 获取商铺装修模板页面list
     *
     * @param param
     * @return Result
     */
    Result listTemplatePage(ShopsRenovationPageParam param);


    /**
     * getByModelId
     *
     * @param modelId
     * @return Result
     */
    Result getByModelId(String modelId);

    /**
     * 获取已经完成装修的商品组件
     *
     * @param param
     * @return
     */
    Result fitmentPrefectureEndPage(ShopsRenovationPageParam param);

    /**
     *  删除专区时匹配删除 装修数据   装修未配置及删除成功同返回true
     * @param modelId
     * @return
     */
    Boolean delShopRenovationPageByModelId(String modelId);
}


