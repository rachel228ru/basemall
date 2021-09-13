package com.medusa.gruul.goods.mapper.manager;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.medusa.gruul.goods.api.entity.Product;
import com.medusa.gruul.goods.api.model.param.manager.ProductParam;
import com.medusa.gruul.goods.api.model.vo.manager.ProductVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 素材库商品信息 Mapper 接口
 * </p>
 *
 * @author alan
 * @since 2019-09-03
 */
@Repository
public interface CsvProductMapper extends BaseMapper<Product> {

    /**
     * 获取csv商品信息list
     *
     * @param page         分页对象
     * @param productParam 商品信息查询参数
     * @return 商品list信息
     */
    List<ProductVo> queryCsvProductList(IPage page, @Param("productParam") ProductParam productParam);

}
