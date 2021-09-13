package com.medusa.gruul.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.medusa.gruul.order.api.entity.OrderEvaluate;
import com.medusa.gruul.order.model.ManageEvaluateVo;
import com.medusa.gruul.order.model.ManageSearchEvaluateDto;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 订单评价 Mapper 接口
 * </p>
 *
 * @author alan
 * @since 2019 -09-02
 */
public interface OrderEvaluateMapper extends BaseMapper<OrderEvaluate> {


    /**
     * 商家查看评价
     *
     * @param page the page
     * @param dto  the dto
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.medusa.gruul.order.model.ManageEvaluateVo> page
     * @author alan
     * @date 2019 /12/3 20:10
     */
    Page<ManageEvaluateVo> searchOrderEvaluate(Page page, @Param(value = "dto") ManageSearchEvaluateDto dto);

    /**
     * 用户查看评价
     *
     * @param page   the page
     * @param userId the user id
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.medusa.gruul.order.model.ManageEvaluateVo> page
     * @author alan
     * @date 2019 /12/4 20:28
     */
    Page<ManageEvaluateVo> userSearchOrderEvaluate(Page page, @Param(value = "userId") String userId);

}
