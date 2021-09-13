package com.medusa.gruul.platform.web.controller;


import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.common.core.monitor.MonitorServiceConfig;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.platform.model.dto.PlatformServiceInfoDto;
import com.medusa.gruul.platform.service.IPlatformServiceInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 服务信息表 前端控制器
 * </p>
 *
 * @author alan
 * @since 2020-02-26
 */
@RestController
@RequestMapping("/platform-service-info")
@Api(tags = "基础库相关接口")
public class PlatformServiceInfoController {


    @Autowired
    private IPlatformServiceInfoService platformServiceInfoService;


    @ApiOperation(value = "基础库心跳统一入口")
    @PostMapping(value = "base-warehouse")
    @EscapeLogin
    public String baseWarehouse(@RequestBody MonitorServiceConfig monitorServiceConfig) {
        platformServiceInfoService.baseWarehouse(monitorServiceConfig);
        return "SUCCESS";
    }

    @ApiOperation(value = "更新服务数据")
    @PutMapping(value = "update/{serviceId}")
    @EscapeLogin
    public Result updateService(@PathVariable(value = "serviceId") Long serviceId, @RequestBody PlatformServiceInfoDto dto) {
        platformServiceInfoService.updateService(serviceId, dto);
        return Result.ok();
    }

    @ApiOperation(value = "删除指定基础库服务")
    @DeleteMapping(value = "/{serviceId}")
    @EscapeLogin
    public Result deleteService(@PathVariable(value = "serviceId") Long serviceId) {
        platformServiceInfoService.deleteService(serviceId);
        return Result.ok();
    }
}
