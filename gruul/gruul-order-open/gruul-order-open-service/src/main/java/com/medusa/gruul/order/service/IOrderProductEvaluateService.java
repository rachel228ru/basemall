package com.medusa.gruul.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.order.api.entity.OrderProductEvaluate;
import com.medusa.gruul.order.model.ApiSearchProductEvaluateDto;
import com.medusa.gruul.order.model.UserEvaluateVo;

/**
 * The interface Order product evaluate service.
 */
public interface IOrderProductEvaluateService extends IService<OrderProductEvaluate> {

    /**
     * 商品详情页评价概况
     *
     * @param productId the product id
     * @return com.medusa.gruul.order.model.UserEvaluateVo user evaluate vo
     * @author alan
     * @date 2020 /1/18 21:49
     */
    UserEvaluateVo userEvaluateOverview(String productId);

    /**
     * 商品详情页评价
     *
     * @param dto the dto
     * @return com.medusa.gruul.common.core.util.PageUtils page utils
     * @author alan
     * @date 2020 /1/18 22:14
     */
    PageUtils productEvaluate(ApiSearchProductEvaluateDto dto);
}
