package com.medusa.gruul.platform.web.controller;


import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.platform.model.dto.ShopTemplateCraeteOrUpdateDto;
import com.medusa.gruul.platform.model.vo.ShopTemplateListVo;
import com.medusa.gruul.platform.service.IPlatformShopTemplateInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 店铺模版表 前端控制器
 * </p>
 *
 * @author whh
 * @since 2020-03-06
 */
@RestController
@RequestMapping("/shopTemplate")
@Api(tags = "模板中心相关接口")
public class PlatformShopTemplateInfoController {

    @Autowired
    private IPlatformShopTemplateInfoService platformShopTemplateInfoService;


    @PostMapping(value = "/create")
    @ApiOperation(value = "新增模板")
    @EscapeLogin
    public Result create(@RequestBody @Validated ShopTemplateCraeteOrUpdateDto sendCodeDto) {
        platformShopTemplateInfoService.create(sendCodeDto);
        return Result.ok();
    }

    @PutMapping(value = "/edit")
    @EscapeLogin
    @ApiOperation(value = "更新模板信息")
    public Result edit(@RequestBody @Validated ShopTemplateCraeteOrUpdateDto sendCodeDto) {
        platformShopTemplateInfoService.edit(sendCodeDto);
        return Result.ok();
    }


    @GetMapping(value = "/list")
    @EscapeLogin
    @ApiOperation(value = "获取模板列表")
    public Result list(@ApiParam(value = "指定页数", required = true) @RequestParam Integer page,
                       @ApiParam(value = "数据条数", required = true) @RequestParam Integer size,
                       @ApiParam(value = "分类类型：0 所有 1 系统模版 2 定制模版", required = true) @RequestParam Integer type) {
        PageUtils<List<ShopTemplateListVo>> vos = platformShopTemplateInfoService.listQuery(page, size, type);
        return Result.ok(vos);
    }


    @DeleteMapping(value = "/{id}")
    @EscapeLogin
    @ApiOperation(value = "删除店铺模板")
    public Result delete(@ApiParam(value = "店铺模板id", required = true) @PathVariable Long id) {
        platformShopTemplateInfoService.deleteById(id);
        return Result.ok();
    }


}
