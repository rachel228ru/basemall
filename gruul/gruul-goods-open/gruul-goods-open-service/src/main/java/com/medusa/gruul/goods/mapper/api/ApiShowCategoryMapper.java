package com.medusa.gruul.goods.mapper.api;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medusa.gruul.goods.api.entity.ShowCategory;
import com.medusa.gruul.goods.api.model.vo.api.ApiShowCategoryVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 小程序商品展示分类 Mapper接口
 *
 * @author kyl
 * @since 2019-11-05
 */
@Repository
public interface ApiShowCategoryMapper extends BaseMapper<ShowCategory> {

    /**
     * 获取所有含有商品的展示分类
     *
     * @return 展示分类list信息
     */
    List<ApiShowCategoryVo> queryAllApiShowCategoryList();

    /**
     * 获取所有含有商超商品的展示分类
     *
     * @param saleMode
     * @return 展示分类list信息
     */
    List<ApiShowCategoryVo> queryApiSupermarketShowCategoryList(Long saleMode);

}
