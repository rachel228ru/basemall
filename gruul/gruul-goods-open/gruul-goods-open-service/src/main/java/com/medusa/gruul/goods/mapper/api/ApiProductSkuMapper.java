package com.medusa.gruul.goods.mapper.api;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.medusa.gruul.goods.api.entity.Product;
import com.medusa.gruul.goods.api.model.vo.api.ApiProductVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 小程序商品信息 Mapper接口
 *
 * @author lcy
 * @since 2020-08-17
 */
@Repository
public interface ApiProductSkuMapper extends BaseMapper<Product> {


}
