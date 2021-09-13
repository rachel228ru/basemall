package com.medusa.gruul.platform.web.controller;

import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.platform.model.dto.GenerateMultipleDto;
import com.medusa.gruul.platform.service.IDefaultValueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 默认数据表 前端控制器
 * </p>
 *
 * @author whh
 * @since 2019-09-07
 */
@RestController
@RequestMapping("/default-value")
@Api(tags = "默认值导入")
public class DefaultValueController {

    @Autowired
    private IDefaultValueService defaultValueService;

    @ApiOperation(value = "导入默认数据json文件")
    @PostMapping("report")
    @EscapeLogin
    public Result reportDefaultValue(@RequestParam("file") MultipartFile file) {
        defaultValueService.reportDefaultValue(file);
        return Result.ok();
    }

    @ApiOperation(value = "获取指定版本默认值")
    @GetMapping("/{uniqueIdentification}/{version}")
    @EscapeLogin
    public Result getValue(@PathVariable(value = "uniqueIdentification") String uniqueIdentification,
                           @PathVariable(value = "version") String version) {
        String json = defaultValueService.getValue(uniqueIdentification, version);
        return Result.ok(json);
    }

    @GetMapping(value = "/validate/code")
    @ApiOperation(value = "验证模板编号是否存在")
    @EscapeLogin
    public Result<Boolean> validateCode(@RequestParam String uniqueIdentification) {
        Boolean flag = defaultValueService.getByUniqueIdentificationIsExit(uniqueIdentification);
        return Result.ok(flag);
    }

    @ApiOperation(value = "生成当前版本指定默认值配置")
    @PostMapping("/generate/multiple")
    @EscapeLogin
    public Result generateMultiple(@RequestBody GenerateMultipleDto generateMultipleDto) {
        defaultValueService.generateMultiple(generateMultipleDto);
        return Result.ok();
    }


}
