package com.medusa.gruul.logistics.mapper;

import com.medusa.gruul.logistics.model.vo.LogisticsExpressAddressVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 物流公司设置表与退发货地址管理表联合查询
 * @author lcysike
 */
@Repository
public interface LogisticsExpressAddressMapper{

    /**
     * 根据物流设置id获取物流公司信息与发货信息组装数据
     * @param expressId
     * @return LogisticsExpressAddressVo
     */
    LogisticsExpressAddressVo queryByExpressId(@Param("expressId") Long expressId);

    /**
     * 根据物流公司code获取物流公司设置信息与发货信息组装数据
     * @param expressCode
     * @return LogisticsExpressAddressVo
     */
    List<LogisticsExpressAddressVo> queryByExpressCode(@Param("expressCode") String expressCode);



}