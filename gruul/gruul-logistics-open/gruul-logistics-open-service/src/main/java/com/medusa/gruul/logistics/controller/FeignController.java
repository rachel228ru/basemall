package com.medusa.gruul.logistics.controller;

import com.alibaba.fastjson.JSON;
import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.logistics.model.dto.manager.LogisticsFreightDto;
import com.medusa.gruul.logistics.model.dto.manager.express.ExpressInfoDto;
import com.medusa.gruul.logistics.model.vo.LogisticsAddressVo;
import com.medusa.gruul.logistics.model.vo.LogisticsTemplateVo;
import com.medusa.gruul.logistics.service.ILogisticsAddressService;
import com.medusa.gruul.logistics.service.ILogisticsExpressService;
import com.medusa.gruul.logistics.service.ILogisticsTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhaozheng
 */
@RestController
@Slf4j
public class FeignController {

    @Autowired
    private ILogisticsTemplateService logisticsTemplateService;

    @Autowired
    private ILogisticsExpressService logisticsExpressService;

    @Autowired
    private ILogisticsAddressService logisticsAddressService;

    /**
     * Description: getLogisticList  获取运费模板列表
     * <p>Version: </p>
     * @author: Mr.zhaozheng
     * <p>Date: 2020-02-12 15:23 </p>
     * @return com.medusa.gruul.common.core.util.ResultList
     */
    @GetMapping(value = "/feign/get/list")
    @EscapeLogin
    public Result<List<LogisticsTemplateVo>> getLogisticList() {
        List<LogisticsTemplateVo> logisticsTemplateVos = logisticsTemplateService.feignGetList();
        return Result.ok(logisticsTemplateVos);
    }

    /**
     * Description: getLogisticsInfo 获取模板详情
     * <p>Version: </p>
     * @author: Mr.zhaozheng
     * <p>Date: 2020-02-12 15:24 </p>
     * @return com.medusa.gruul.common.core.util.Result
     */
    @GetMapping(value = "/feign/get/info")
    @EscapeLogin
    public Result<LogisticsTemplateVo> getLogisticsInfo(@RequestParam("id") Long id) {
        LogisticsTemplateVo logisticsTemplateVo = logisticsTemplateService.feignGetInfo(id);
        return Result.ok(logisticsTemplateVo);

    }

    /**
     * Description: getLogisticsInfo 生成快递公司快递单号
     * @author: lcysike
     * <p>Date: 2020-03-09 09:50 </p>
     * @return com.medusa.gruul.common.core.util.Result
     */
    @GetMapping(value = "/feign/get/express/number")
    @EscapeLogin
    public Result getLogisticsExpressNumber(@RequestParam("expressInfoDto") ExpressInfoDto expressInfoDto
              , @RequestParam("addressId") Long addressId,@RequestParam("shopId") String shopId) {
        return logisticsAddressService.getLogisticsExpressNumber(expressInfoDto, addressId,shopId);
    }

    /**
     * Description: getLogisticsInfo 根据快递公司代码与运单号获取快递物流信息
     * @author: lcysike
     * <p>Date: 2020-03-09 09:50 </p>
     * @return com.medusa.gruul.common.core.util.Result
     */
    @GetMapping(value = "/feign/get/express/route")
    @EscapeLogin
    public Result getLogisticsExpressInfo(@RequestParam("waybillNo") String waybillNo, @RequestParam("shipperType") String shipperType) {
        return logisticsExpressService.getLogisticsExpressRoute(waybillNo, shipperType);
    }

    /***
    * @description: 运费计算接口
    * @return: com.medusa.gruul.common.core.util.ResultList
    * @throws:@RequestParam("logisticsFreightDto") List<LogisticsFreightDto> logisticsFreightDtos,String region(运送地区 json 格式 {"provinceid": ["cityId","cityId2"]})
    * @author: wangpeng
    * @version : v1.0
    * @date: 2020/2/29 11:10 上午
    */
    @GetMapping(value = "/feign/freight/calculation")
    @EscapeLogin
     public BigDecimal getLogisticsFreightCalculation(@RequestParam("logisticsFreightDtos")String logisticsFreightDtos, @RequestParam("region")String region){
         List<LogisticsFreightDto> logisticsFreightDtosList = JSON.parseArray(logisticsFreightDtos, LogisticsFreightDto.class);
        return  logisticsTemplateService.freightCalculation(logisticsFreightDtosList,region);
    }

    /**
     * 获取默认发/收货地址
     * @param type 收发货类型 1-发货地址 2-收货地址
     * @return LogisticsAddressVo
     */
    @GetMapping(value = "/feign/get/default/address")
    @EscapeLogin
    public LogisticsAddressVo getFeignDefaultAddress(@RequestParam("type") Integer type){
        LogisticsAddressVo logisticsAddressVo = logisticsAddressService.getDefaultAddress(type);
        return logisticsAddressVo;
    }

}
