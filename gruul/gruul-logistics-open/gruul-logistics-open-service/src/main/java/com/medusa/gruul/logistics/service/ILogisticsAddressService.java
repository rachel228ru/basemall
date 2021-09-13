package com.medusa.gruul.logistics.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.logistics.model.dto.manager.LogisticsAddressDto;
import com.medusa.gruul.logistics.model.dto.manager.LogisticsBatchDeliverDto;
import com.medusa.gruul.logistics.model.dto.manager.LogisticsPrintDeliverDto;
import com.medusa.gruul.logistics.model.dto.manager.express.ExpressInfoDto;
import com.medusa.gruul.logistics.model.param.LogisticsAddressParam;
import com.medusa.gruul.logistics.model.vo.LogisticsAddressVo;

import java.util.List;
import java.util.Map;

/**
 * 收发货地址管理
 * @author zhaozheng
 */
public interface ILogisticsAddressService {
    /**
     * Description: getAddressList
     * <p>Version: </p>
     * <p>Author: Mr.zhaozheng </p>
     * <p>Date: 2020-02-11 16:10 </p>
     * @param logisticsAddressParam
     * @return com.medusa.gruul.common.core.util.ResultList
     */
    IPage<LogisticsAddressVo> getAddressList(LogisticsAddressParam logisticsAddressParam);

    /**
     * 查看所有地址
     * @return List<LogisticsAddressVo>
     */
    List<LogisticsAddressVo> getAllAddressList();

    /**
     * 更新/新增 地址
     * @param logisticsAddressDto
     */
    void setAddress(LogisticsAddressDto logisticsAddressDto);

    /**
     * 设置 默认地址
     * @param type 1-发货地址 2-收货地址
     * @param id
     */
    void setDefAddress(Integer type, Long id);


    /**
     * 删除地址
     * @param id 主键
     */
    void delAddress(Long id);

    /**
     * 获取默认收/发货地址
     *
     * @param type 收发货类型 1-发货地址 2-收货地址
     * @return LogisticsAddressVo
     */
    LogisticsAddressVo getDefaultAddress(Integer type);

    /**
     * 获取发货所需
     * @param shopId 店铺
     * @param tenantId
     * @return
     */
    Map<String, Object> listLogisticsCompany(String shopId,String tenantId);

    /**
     * 设置默认快递公司
     * @param logisticsCompanyId
     * @author: wangpeng
     * @version : v1.0
     * @date: 2020/3/13 8:35 下午
     */
    void setCompanyDefault(Long logisticsCompanyId);

    /***
    * 打印并发货
     * @param logisticsPrintDeliverDto
    * @author: wangpeng
    * @version : v1.0
    * @date: 2020/3/13 9:46 下午
    */
    void doPrintDeliverGoods(LogisticsPrintDeliverDto logisticsPrintDeliverDto);

    /** 
    * 批量发货
    * @param logisticsBatchDeliverDtos
    * @param shopId
     * @param tenantId
     * @author: wangpeng
    * @version : v1.0      
    * @date: 2020/3/14 4:47 下午
    */ 
    void doBatchDeliver(List<LogisticsBatchDeliverDto> logisticsBatchDeliverDtos, String shopId, String tenantId);

    /**
     * 生成快递公司快递单号
     * @param expressInfoDto 订单传输信息
     * @param addressId 发货地址id
     * @param shopId
     * @return Result
     */
    Result getLogisticsExpressNumber(ExpressInfoDto expressInfoDto, Long addressId, String shopId);

}
