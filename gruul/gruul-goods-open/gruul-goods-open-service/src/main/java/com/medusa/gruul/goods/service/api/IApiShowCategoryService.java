package com.medusa.gruul.goods.service.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.goods.api.entity.ShowCategory;
import com.medusa.gruul.goods.api.model.vo.api.ApiShowCategoryVo;

import java.util.List;

/**
 * 小程序商品展示分类 服务类
 *
 * @author kyl
 * @since 2019-11-05
 */
public interface IApiShowCategoryService extends IService<ShowCategory> {

    /**
     * 获取所有含有商品的展示分类
     *
     * @return 展示分类list信息
     */
    List<ApiShowCategoryVo> getAllApiShowCategoryList();

    /**
     * 获取对应分区含有商品的展示分类
     *
     * @param saleMode
     * @return 展示分类list信息
     */
    List<ApiShowCategoryVo> getApiShowCategoryListBySaleMode(Long saleMode);
}
