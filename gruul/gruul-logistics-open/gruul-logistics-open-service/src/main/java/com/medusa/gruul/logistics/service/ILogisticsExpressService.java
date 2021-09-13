package com.medusa.gruul.logistics.service;


import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.logistics.model.dto.manager.LogisticsExpressDto;
import com.medusa.gruul.logistics.model.param.LogisticsExpressParam;
import com.medusa.gruul.logistics.model.vo.LogisticsExpressVo;

import java.util.List;

/**
 * @author lcysike
 */
public interface ILogisticsExpressService {

    /**
     * 物流公司信息列表查询
     * @param logisticsExpressParam
     * @return List<LogisticsAddressVo>
     */
    List<LogisticsExpressVo> getExpressList(LogisticsExpressParam logisticsExpressParam);

    /**
     * 查询单条物流公司信息
     * @param id
     * @return LogisticsAddressVo
     */
    LogisticsExpressVo getExpressInfo(Long id);

    /**
     * 更新/新增 快递公司
     * @param logisticsExpressDto
     */
    void setExpressInfo(LogisticsExpressDto logisticsExpressDto);

    /**
     * 删除快递公司信息
     * @param id 主键
     */
    void delExpressInfo(Long id);

    /**
     * 根据快递公司代码与运单号获取快递物流信息
     * @param waybillNo   运单号
     * @param shipperType 快递公司代码
     * @return Result
     */
    Result getLogisticsExpressRoute(String waybillNo, String shipperType);
}
