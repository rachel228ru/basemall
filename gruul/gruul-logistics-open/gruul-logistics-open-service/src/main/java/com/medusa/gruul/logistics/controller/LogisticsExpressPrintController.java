package com.medusa.gruul.logistics.controller;

import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.logistics.model.dto.manager.LogisticsExpressDto;
import com.medusa.gruul.logistics.model.dto.manager.LogisticsExpressPrintDto;
import com.medusa.gruul.logistics.model.param.LogisticsExpressParam;
import com.medusa.gruul.logistics.model.param.LogisticsExpressPrintParam;
import com.medusa.gruul.logistics.model.vo.LogisticsExpressPrintVo;
import com.medusa.gruul.logistics.model.vo.LogisticsExpressVo;
import com.medusa.gruul.logistics.service.ILogisticsExpressPrintService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 电子面单打印设置
 * @author lcysike
 */
@RestController
public class LogisticsExpressPrintController {

    @Autowired
    private ILogisticsExpressPrintService logisticsExpressPrintService;

    /**
     * 电子面单打印设置列表查询
     */
    @GetMapping(value = "/get/express/print/list")
    @ApiOperation(value = "电子面单打印设置列表查询")
    public Result<PageUtils<LogisticsExpressPrintVo>> getExpressPrintList(LogisticsExpressPrintParam logisticsExpressPrintParam) {
        PageUtils<LogisticsExpressPrintVo> pageUtils = new PageUtils(logisticsExpressPrintService.getExpressPrintList(logisticsExpressPrintParam));
        return Result.ok(pageUtils);
    }

    /**
     * 查询单条电子面单打印机信息
     */
    @PostMapping(value = "/get/express/print/info/{id}")
    @ApiOperation("查询单条电子面单打印机信息")
    public Result<LogisticsExpressPrintVo> getExpressPrintInfo(@ApiParam(value = "打印机id", required = true) @PathVariable("id") Long id) {
        LogisticsExpressPrintVo logisticsExpressPrintVo = this.logisticsExpressPrintService.getExpressPrintInfo(id);
        return Result.ok(logisticsExpressPrintVo);
    }


    /**
     * 新增/修改 电子面单打印设置
     */
    @PostMapping(value = "/set/express/print/info")
    @ApiOperation("新增/修改电子面单打印设置")
    public Result setExpressPrintInfo(@RequestBody LogisticsExpressPrintDto logisticsExpressPrintDto) {
        this.logisticsExpressPrintService.setExpressPrintInfo(logisticsExpressPrintDto);
        return Result.ok();
    }

    /**
     * 启用/停用电子面单打印设置
     *
     * @param type 0-停用 1-启用
     * @param id
     * @return
     */
    @PutMapping(value = "/set/express/print/status/{id}/{type}")
    @ApiOperation("设置启用/停用打印机")
    public Result setExpressPrintStatus(@ApiParam(value = "打印机id", required = true) @PathVariable("id") Long id,
                                @ApiParam(value = "收发货类型 0-停用 1-启用", required = true) @PathVariable("type") Integer type) {
        this.logisticsExpressPrintService.setExpressPrintStatus(type, id);
        return Result.ok();
    }


    /**
     * 删除电子面单打印设置
     * @param id
     */
    @DeleteMapping(value = "/del/express/print/{id}")
    @ApiOperation(value = "删除电子面单打印机设置")
    public Result delExpressPrintInfo(@ApiParam(value = "打印机id", required = true) @PathVariable("id") Long id) {
        this.logisticsExpressPrintService.delExpressPrintInfo(id);
        return Result.ok();
    }

}
