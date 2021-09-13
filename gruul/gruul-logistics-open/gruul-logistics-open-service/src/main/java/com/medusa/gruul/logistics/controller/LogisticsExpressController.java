package com.medusa.gruul.logistics.controller;

import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.logistics.model.dto.manager.LogisticsExpressDto;
import com.medusa.gruul.logistics.model.param.LogisticsExpressParam;
import com.medusa.gruul.logistics.model.vo.LogisticsExpressVo;
import com.medusa.gruul.logistics.service.ILogisticsExpressService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 物流公司发货地址设置
 * @author lcysike
 */
@RestController
public class LogisticsExpressController {

    @Autowired
    private ILogisticsExpressService logisticsExpressService;

    /**
     * 物流公司信息列表
     */
    @GetMapping(value = "/get/express/list")
    @ApiOperation(value = "物流公司信息列表查询")
    public Result<List<LogisticsExpressVo>> getExpressList(LogisticsExpressParam logisticsExpressParam) {
        return Result.ok(logisticsExpressService.getExpressList(logisticsExpressParam));
    }

    /**
     * 查询单条物流公司信息
     */
    @PostMapping(value = "/get/express/info/{id}")
    @ApiOperation("查询单条物流公司信息")
    public Result<LogisticsExpressVo> getExpressInfo(@ApiParam(value = "物流公司信息地址id", required = true) @PathVariable("id") Long id) {
        LogisticsExpressVo logisticsExpressVo = this.logisticsExpressService.getExpressInfo(id);
        return Result.ok(logisticsExpressVo);
    }


    /**
     * 新增/修改 物流公司信息
     */
    @PostMapping(value = "/set/express/info")
    @ApiOperation("新增/修改物流公司信息")
    public Result setExpressInfo(@RequestBody LogisticsExpressDto logisticsExpressDto) {
        this.logisticsExpressService.setExpressInfo(logisticsExpressDto);
        return Result.ok();
    }


    /**
     * 删除物流公司信息
     * @param id
     */
    @DeleteMapping(value = "/del/express/{id}")
    @ApiOperation(value = "删除物流公司信息")
    public Result delExpressInfo(@ApiParam(value = "物流公司信息地址id", required = true) @PathVariable("id") Long id) {
        logisticsExpressService.delExpressInfo(id);
        return Result.ok();
    }

}
