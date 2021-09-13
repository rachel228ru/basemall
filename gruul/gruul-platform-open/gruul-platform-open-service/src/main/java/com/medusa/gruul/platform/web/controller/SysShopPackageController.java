package com.medusa.gruul.platform.web.controller;


import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.platform.model.dto.SysShopPackageUpdateDto;
import com.medusa.gruul.platform.model.vo.SysShopPackageListVo;
import com.medusa.gruul.platform.model.vo.SysShopPackageVo;
import com.medusa.gruul.platform.service.ISysShopPackageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 店铺套餐 前端控制器
 * </p>
 *
 * @author whh
 * @since 2020-08-01
 */
@RestController
@RequestMapping("/sys-shop-package")
@Api(tags = "套餐相关接口")
public class SysShopPackageController {

    @Autowired
    private ISysShopPackageService sysShopPackageService;


    @GetMapping
    @EscapeLogin
    @ApiOperation(value = "查询指定模板版本套餐")
    public Result<List<SysShopPackageListVo>> templateVersionPackages(@RequestParam(name = "templateVersionId") Long templateVersionId) {
        List<SysShopPackageListVo> sysShopPackageListVos = sysShopPackageService.templateVersionPackages(templateVersionId);
        return Result.ok(sysShopPackageListVos);
    }

    @GetMapping("/template")
    @EscapeLogin
    @ApiOperation(value = "查询指定模板套餐")
    public Result<List<SysShopPackageVo>> teamplteIdLastPackages(@RequestParam(name = "templateId") Long templateId) {
        List<SysShopPackageVo> sysShopPackageListVos = sysShopPackageService.getByTeamplteIdLastPackage(templateId);
        return Result.ok(sysShopPackageListVos);
    }


    @GetMapping("/{packageId}")
    @EscapeLogin
    @ApiOperation(value = "查询指定套餐")
    public Result<SysShopPackageVo> findByPackageId(@PathVariable(name = "packageId") Long packageId) {
        SysShopPackageVo sysShopPackageVo = sysShopPackageService.findByPackageId(packageId);
        return Result.ok(sysShopPackageVo);
    }


    @PutMapping
    @EscapeLogin
    @ApiOperation(value = "更新套餐数据")
    public Result updatePackageInfo(@RequestBody @Validated SysShopPackageUpdateDto sysShopPackageUpdateDto) {
        sysShopPackageService.updatePackageInfo(sysShopPackageUpdateDto);
        return Result.ok();
    }


}
