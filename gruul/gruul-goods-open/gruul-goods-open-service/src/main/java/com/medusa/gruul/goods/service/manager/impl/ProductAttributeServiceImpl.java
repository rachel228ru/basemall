package com.medusa.gruul.goods.service.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.goods.api.entity.ProductAttribute;
import com.medusa.gruul.goods.api.model.vo.manager.ProductAttributeVo;
import com.medusa.gruul.goods.mapper.manager.ProductAttributeMapper;
import com.medusa.gruul.goods.service.manager.IProductAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品模版信息 服务实现类
 * </p>
 *
 * @author lcysike
 * @since 2019-09-30
 */
@Service
public class ProductAttributeServiceImpl extends ServiceImpl<ProductAttributeMapper, ProductAttribute> implements IProductAttributeService {

    @Autowired
    private ProductAttributeMapper productAttributeMapper;


    /**
     * 根据商品id获取商品属性信息
     *
     * @param productId
     * @return 商品属性list
     */
    @Override
    public List<ProductAttributeVo> selectByProductId(Long productId) {
        return productAttributeMapper.queryByProductId(productId);
    }

}
