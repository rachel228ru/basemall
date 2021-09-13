package com.medusa.gruul.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.medusa.gruul.order.api.entity.OrderProductEvaluate;
import com.medusa.gruul.order.api.model.ProductRateVo;
import com.medusa.gruul.order.model.ProductEvaluateVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 订单商品评价 接口
 * </p>
 *
 * @author alan
 * @since 2019 -09-02
 */
public interface OrderProductEvaluateMapper extends BaseMapper<OrderProductEvaluate> {

    /**
     * selectByOrderId
     *
     * @param orderId the order id
     * @return java.util.List<com.medusa.gruul.order.api.entity.OrderProductEvaluate> list
     * @author alan
     * @date 2019 /12/3 21:01
     */
    List<OrderProductEvaluate> selectByOrderId(@Param(value = "orderId") Long orderId);

    /**
     * selectProductEvaluatePage
     *
     * @param page      the page
     * @param type      the type
     * @param productId the product id
     * @param userId    the user id
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.medusa.gruul.order.model.ProductEvaluateVo> page
     * @author alan
     * @date 2020 /1/18 22:23
     */
    Page<ProductEvaluateVo> selectProductEvaluatePage(Page page,
                                                      @Param(value = "type") Integer type,
                                                      @Param(value = "productId") String productId,
                                                      @Param(value = "userId") String userId);

    /**
     * selectProductRateByList
     *
     * @param productIds the product ids
     * @return java.util.List<com.medusa.gruul.order.api.model.ProductRateVo> list
     * @author alan
     * @date 2020 /2/7 20:23
     */
    List<ProductRateVo> selectProductRateByList(@Param(value = "productIds") List<Long> productIds);

    /**
     * countTotal
     *
     * @param userId    the user id
     * @param productId the product id
     * @return int int
     * @author alan
     * @date 2020 /2/11 21:18
     */
    int countTotal(@Param(value = "userId") String userId, @Param(value = "productId") String productId);

    /**
     * countPraiseNum
     *
     * @param userId    the user id
     * @param productId the product id
     * @return int int
     * @author alan
     * @date 2020 /2/11 21:18
     */
    int countPraiseNum(@Param(value = "userId") String userId, @Param(value = "productId") String productId);

    /**
     * countHasContent
     *
     * @param userId    the user id
     * @param productId the product id
     * @return int int
     * @author alan
     * @date 2020 /2/11 21:25
     */
    int countHasContent(@Param(value = "userId") String userId, @Param(value = "productId") String productId);

    /**
     * counthasPicture
     *
     * @param userId    the user id
     * @param productId the product id
     * @return int int
     * @author alan
     * @date 2020 /2/11 21:25
     */
    int countHasPicture(@Param(value = "userId") String userId, @Param(value = "productId") String productId);
}
