package com.medusa.gruul.afs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.afs.api.entity.AfsOrderItem;
import com.medusa.gruul.afs.mapper.AfsOrderItemMapper;
import com.medusa.gruul.afs.service.IAfsOrderItemService;
import com.medusa.gruul.afs.service.IAfsOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 提货点申请的详情 服务实现类
 * </p>
 *
 * @author alan
 * @since 2020 -08-21
 */
@Service
public class AfsOrderItemServiceImpl extends ServiceImpl<AfsOrderItemMapper, AfsOrderItem> implements IAfsOrderItemService {


    @Resource
    private IAfsOrderService afsOrderService;


    /**
     * 根据原始订单ID查询换货单的ID
     *
     * @param orderId
     * @return java.util.List<java.lang.Long>
     * @author alan
     * @date 2021/3/17 22:36
     */
    @Override
    public List<Long> getExchangeOrder(Long orderId) {
        return baseMapper.selectExchangeOrderIdsByOriginalOrderId(orderId);
    }


    /**
     *  userApply  用户售后
     *
     * @param orderId
     */
    @Override
    public HashMap<String, Object> userApply(Long orderId) {
        List<Map<String, String>> maps = baseMapper.userApplyItem(orderId);
        Boolean afsExpire = afsOrderService.getAfsExpire(orderId);
        HashMap<String, Object> res = new HashMap<>(2);
        res.put("number", maps);
        res.put("expire", afsExpire);
        return res ;
    }
}
