package com.medusa.gruul.goods.mapper.api;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medusa.gruul.goods.api.entity.ShoppingCart;
import com.medusa.gruul.goods.api.model.vo.api.ApiShoppingCartVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 小程序会员购物车信息 Mapper接口
 *
 * @author lcy
 * @since 2019-11-15
 */
@Repository
public interface ApiShoppingCartMapper extends BaseMapper<ShoppingCart> {

    /**
     * 根据会员id获取会员的购物车信息
     *
     * @param map
     * @return 购物车商品list信息
     */
    @SqlParser(filter = true)
    List<ApiShoppingCartVo> queryShoppingCartListByUserId(Map map);

    /**
     * 根据会员id获取会员的购物车失效商品信息
     *
     * @param map
     * @return 购物车商品list信息
     */
    @SqlParser(filter = true)
    List<ApiShoppingCartVo> selectEffectShoppingCart(Map map);
}
