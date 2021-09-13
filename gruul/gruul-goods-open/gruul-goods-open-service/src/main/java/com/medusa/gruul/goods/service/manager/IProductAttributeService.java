package com.medusa.gruul.goods.service.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.goods.api.entity.ProductAttribute;
import com.medusa.gruul.goods.api.model.vo.manager.ProductAttributeVo;

import java.util.List;

/**
 * <p>
 * 商品模版信息 服务类
 * </p>
 *
 * @author lcysike
 * @since 2019-09-03
 */
public interface IProductAttributeService extends IService<ProductAttribute> {

    /**
     * 查询列表
     *
     * @param productId
     * @return 产品模版信息对象
     */
    List<ProductAttributeVo> selectByProductId(Long productId);

}
