package com.medusa.gruul.platform.web.controller;


import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.platform.model.dto.ShopTemplateVersionOptionDto;
import com.medusa.gruul.platform.model.vo.BaseLibrariesVo;
import com.medusa.gruul.platform.model.vo.SkipUrlVo;
import com.medusa.gruul.platform.service.IPlatformShopTemplateDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 店铺模版详情表 前端控制器
 * </p>
 *
 * @author whh
 * @since 2020-03-06
 */
@RestController
@RequestMapping("/shopTemplate/version")
@Api(tags = "模板中心相关接口")
public class PlatformShopTemplateDetailController {

    @Autowired
    private IPlatformShopTemplateDetailService platformShopTemplateDetailService;

    @PostMapping
    @EscapeLogin
    @ApiOperation(value = "新增模板版本")
    public Result create(@RequestBody @Validated ShopTemplateVersionOptionDto dto) {
        platformShopTemplateDetailService.create(dto);
        return Result.ok();
    }

    @PutMapping
    @EscapeLogin
    @ApiOperation(value = "编辑模板版本")
    public Result edit(@RequestBody @Validated ShopTemplateVersionOptionDto dto) {
        platformShopTemplateDetailService.edit(dto);
        return Result.ok();
    }

    @DeleteMapping(value = "/{id}")
    @EscapeLogin
    @ApiOperation(value = "删除模板版本")
    public Result deleteById(@ApiParam(value = "模板版本id", required = true) @PathVariable Long id) {
        platformShopTemplateDetailService.deleteById(id);
        return Result.ok();
    }

    @ApiOperation(value = "获取当前版本配置的跳转地址")
    @GetMapping
    @EscapeLogin
    public Result<SkipUrlVo> getSkipUrl() {
        SkipUrlVo vo = platformShopTemplateDetailService.getSkipUrl();
        return Result.ok(vo);
    }

}
