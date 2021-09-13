package com.medusa.gruul.logistics.controller;

import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.logistics.model.dto.manager.LogisticsTemplateDto;
import com.medusa.gruul.logistics.model.param.LogisticsTemplateParam;
import com.medusa.gruul.logistics.model.vo.LogisticsTemplateVo;
import com.medusa.gruul.logistics.service.ILogisticsTemplateService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 物流模版设置控制层
 * @author lcysike
 */
@RestController
public class LogisticsTemplateController {
    @Autowired
    private ILogisticsTemplateService logisticsTemplateService;

    /**
     * 获取模板列表
     *
     * @param logisticsTemplateParam
     * @return Result<PageUtils>
     */

    @GetMapping(value = "/get/template/list")
    @ApiOperation(value = "获取模板列表")
    public Result<PageUtils<LogisticsTemplateVo>> getTemplateList(LogisticsTemplateParam logisticsTemplateParam) {
        PageUtils<LogisticsTemplateVo> pageUtils = new PageUtils(logisticsTemplateService.getTemplateList(logisticsTemplateParam));
        return Result.ok(pageUtils);
    }

    /**
     * 获取单个模板详情
     *
     * @param id
     * @return
     */

    @GetMapping(value = "/get/template/info/{id}")
    @ApiOperation(value = "获取单个模板详情")
    public Result<LogisticsTemplateVo> getTemplateInfo(@ApiParam(value = "模版id", required = true) @PathVariable("id") Long id) {
        LogisticsTemplateVo logisticsTemplateVo = this.logisticsTemplateService.getTemplateInfo(id);
        return Result.ok(logisticsTemplateVo);
    }

    /**
     * 新增/修改单个模板模板
     *
     * @param logisticsTemplateDto
     * @return
     */

    @PostMapping(value = "/set/template/info")
    @ApiOperation(value = "新增/修改单个模板信息")
    public Result setTemplateInfo(@RequestBody LogisticsTemplateDto logisticsTemplateDto) {
        this.logisticsTemplateService.addOrUpdateTemplate(logisticsTemplateDto);
        return Result.ok();
    }

    /**
     * 删除模板
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除模板")
    @DeleteMapping(value = "/delete/template/info/{id}")
    public Result removeTemplateInfo(@ApiParam(value = "模版id", required = true) @PathVariable("id") Long id) {
        logisticsTemplateService.removeTemplateById(id);
        return Result.ok();
    }
}
