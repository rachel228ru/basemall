package com.medusa.gruul.goods.mapper.manager;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medusa.gruul.goods.api.entity.ProductAttribute;
import com.medusa.gruul.goods.api.model.vo.manager.ProductAttributeVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 商品模版信息 Mapper 接口
 * </p>
 *
 * @author lcysike
 * @since 2019-09-30
 */
@Repository
public interface ProductAttributeMapper extends BaseMapper<ProductAttribute> {

    /**
     * 获取指定商品模版信息
     *
     * @param productId 商品id
     * @return 商品模版list信息
     */
    List<ProductAttributeVo> queryByProductId(Long productId);

}
