package com.medusa.gruul.goods.mapper.api;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.medusa.gruul.goods.api.entity.Product;
import com.medusa.gruul.goods.api.model.param.api.ApiProductParam;
import com.medusa.gruul.goods.api.model.vo.api.ApiProductVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 小程序商品信息 Mapper接口
 *
 * @author kyl
 * @since 2019-10-06
 */
@Repository
public interface ApiProductMapper extends BaseMapper<Product> {

    /**
     * 获取商品详情
     *
     * @param id 商品id
     * @return 商品信息
     */
    ApiProductVo queryByPrimaryKey(Long id);

    /**
     * 获取商品列表
     *
     * @param page
     * @param apiProductParam
     * @return 商品list信息
     */
    List<ApiProductVo> queryList(IPage page, @Param("apiProductParam") ApiProductParam apiProductParam);


}
