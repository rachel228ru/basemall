package com.medusa.gruul.logistics.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.medusa.gruul.logistics.api.entity.LogisticsAddress;
import com.medusa.gruul.logistics.model.param.LogisticsAddressParam;
import com.medusa.gruul.logistics.model.vo.LogisticsAddressVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author zhaozheng
 */
@Repository
public interface LogisticsAddressMapper extends BaseMapper<LogisticsAddress> {

    /**
     * 查询收发货地址列表
     * @param page
     * @param logisticsAddressParam
     * @return List<LogisticsAddressVo>
     */
    List<LogisticsAddressVo> queryLogisticsAddressList(IPage page, @Param("logisticsAddressParam") LogisticsAddressParam logisticsAddressParam);

    /**
     * 查询所有地址
     * @return List<LogisticsAddressVo>
     */
    List<LogisticsAddressVo> queryAllLogisticsAddressList();


    /**
     * 根据快递公司代码获取关联的地址信息
     * @param page
     * @param expressCode 快递公司代码
     * @return List<LogisticsAddressVo>
     */
    List<LogisticsAddressVo> queryLogisticsAddressByExpressCode(IPage page, @Param("expressCode") String expressCode);


    /**
     * 获取默认收/发货地址
     * @param type 收发货类型 1-发货地址 2-收货地址
     * @return List<LogisticsAddressVo>
     */
    LogisticsAddressVo queryDefaultAddress(@Param("type") Integer type);


}