package com.medusa.gruul.logistics.controller;

import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.logistics.model.dto.manager.LogisticsAddressDto;
import com.medusa.gruul.logistics.model.dto.manager.LogisticsBatchDeliverDto;
import com.medusa.gruul.logistics.model.dto.manager.LogisticsPrintDeliverDto;
import com.medusa.gruul.logistics.model.param.LogisticsAddressParam;
import com.medusa.gruul.logistics.model.vo.LogisticsAddressVo;
import com.medusa.gruul.logistics.service.ILogisticsAddressService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;


/**
 * 收发货地址管理控制层
 * @author lcysike
 */
@RestController
public class LogisticsAddressController {
    @Autowired
    private ILogisticsAddressService logisticsAddressService;

    /**
     * 物流-查看地址列表
     *
     * @return
     */
    @GetMapping(value = "/get/address/list")
    @ApiOperation(value = "物流-查看地址列表")
    public Result<PageUtils<LogisticsAddressVo>> getAddressList(LogisticsAddressParam logisticsAddressParam) {
        PageUtils<LogisticsAddressVo> pageUtils = new PageUtils(logisticsAddressService.getAddressList(logisticsAddressParam));
        return Result.ok(pageUtils);
    }

    /**
     * 物流-查看所有地址
     *
     * @return
     */
    @GetMapping(value = "/get/all/address")
    @ApiOperation(value = "物流-查看所有地址")
    public Result<List<LogisticsAddressVo>> getAllAddressList() {
        List<LogisticsAddressVo> logisticsAddressVos = logisticsAddressService.getAllAddressList();
        return Result.ok(logisticsAddressVos);
    }


    /**
     * 新增/修改 地址
     *
     * @return
     */
    @PostMapping(value = "/set/address")
    @ApiOperation("新增/修改地址")
    public Result setAddress(@RequestBody LogisticsAddressDto logisticsAddressDto) {
        this.logisticsAddressService.setAddress(logisticsAddressDto);
        return Result.ok();
    }

    /**
     * 设置默认发货/退货地址
     *
     * @param type 1-发货地址 2-收货地址
     * @param id
     * @return
     */
    @PutMapping(value = "/set/def/address/{id}/{type}")
    @ApiOperation("设置默认发货/退货地址")
    public Result setDefAddress(@ApiParam(value = "收发货地址id", required = true) @PathVariable("id") Long id,
                                @ApiParam(value = "收发货类型 1-发货地址 2-收货地址", required = true) @PathVariable("type") Integer type) {
        logisticsAddressService.setDefAddress(type, id);
        return Result.ok();
    }


    /**
     * 删除地址
     *
     * @param id
     */
    @DeleteMapping(value = "/del/address/{id}")
    @ApiOperation(value = "删除地址")
    public Result delAddress(@ApiParam(value = "收发货地址id", required = true) @PathVariable("id") Long id) {
        logisticsAddressService.delAddress(id);
        return Result.ok();
    }

    /**
     * 物流-获取默认发/收货地址
     *
     * @return
     */
    @GetMapping(value = "/get/default/address/{type}")
    @ApiOperation(value = "物流-获取默认发/收货地址")
    public LogisticsAddressVo getDefaultAddress(@ApiParam(value = "收发货类型 1-发货地址 2-收货地址", required = true) @PathVariable("type") Integer type) {
        LogisticsAddressVo logisticsAddressVo = logisticsAddressService.getDefaultAddress(type);
        return logisticsAddressVo;
    }

    /**
     * 发货页面
     *
     * @return
     */
    @GetMapping(value = "/list/company")
    @ApiOperation(value = "发货页面")
    public Result listLogisticsCompany() {
        String shopId = ShopContextHolder.getShopId();
        String tenantId = TenantContextHolder.getTenantId();
        Map<String, Object> res = logisticsAddressService.listLogisticsCompany(shopId,tenantId);
        return Result.ok(res);
    }


    /**
     * 设为默认发货物流公司
     *
     * @return
     */
    @GetMapping(value = "/set/default")
    @ApiOperation(value = "设为默认发货物流公司")
    public Result setCompanyDefault(Long logisticsCompanyId ) {
        logisticsAddressService.setCompanyDefault(logisticsCompanyId);
        return Result.ok();
    }

    /**
     * 打印并发货(单个)
     * @return
     */
    @PostMapping(value = "/print/deliver/goods")
    @ApiOperation(value = "打印并发货")
    public Result doDeliverGoods(@RequestBody @Valid LogisticsPrintDeliverDto logisticsPrintDeliverDto) {
        logisticsPrintDeliverDto.setShopId(ShopContextHolder.getShopId());
        logisticsPrintDeliverDto.setTenantId(TenantContextHolder.getTenantId());
        logisticsAddressService.doPrintDeliverGoods(logisticsPrintDeliverDto);
        return Result.ok();
    }


    /**
     * 批量发货
     *
     * @return
     */
    @PostMapping(value = "/batch/deliver/goods")
    @ApiOperation(value = "批量发货")
    public Result doBatchDeliver(@RequestBody List<LogisticsBatchDeliverDto> logisticsBatchDeliverDtos) {
        String shopId = ShopContextHolder.getShopId();
        String tenantId = TenantContextHolder.getTenantId();
        logisticsAddressService.doBatchDeliver(logisticsBatchDeliverDtos,shopId,tenantId);
        return Result.ok();
    }
}

