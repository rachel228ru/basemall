package com.medusa.gruul.platform.web.controller;

import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.platform.model.dto.MiniInfoUpdateDto;
import com.medusa.gruul.platform.model.dto.WxaGetwxacode;
import com.medusa.gruul.platform.model.vo.BaseInfoVo;
import com.medusa.gruul.platform.service.IMiniInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 小程序相关接口
 * </p>
 *
 * @author whh
 * @since 2019-09-07
 */
@RestController
@RequestMapping("/mini-info")
@Api(tags = "小程序相关接口")
public class MiniInfoController {

    @Autowired
    private IMiniInfoService miniInfoService;


    @PutMapping("update")
    @ApiOperation(value = "更新小程序数据")
    public Result updateMini(@RequestBody MiniInfoUpdateDto miniInfoUpdateDto) {
        miniInfoService.updateMini(miniInfoUpdateDto);
        return Result.ok();
    }


    @PostMapping("wxa/getwxacode")
    @ApiOperation(value = "获取小程序码,返回base64")
    public Result wxaGetwxacode(@RequestBody @Validated WxaGetwxacode wxaGetwxacode) {
        //Todo 没走开放平台得 该处请替换为小程序相关方法
        return miniInfoService.wxaGetwxacode(wxaGetwxacode);
    }


    @GetMapping("/base/info")
    @ApiOperation(value = "获取绑定的小程序信息|公众号信息")
    public Result<BaseInfoVo> baseInfo(@ApiParam(value = "获取信息类型,1-公众号 2-小程序", required = true) @RequestParam Integer type) {
        BaseInfoVo infoVo = miniInfoService.baseInfo(type);
        return Result.ok(infoVo);
    }

    @GetMapping("/bind/open/info")
    @ApiOperation(value = "获取当前店铺小程序和公众号是否已经进行绑定,  data = true or false")
    public Result<Boolean> bindOpenInfo() {
        String tenantId = TenantContextHolder.getTenantId();
        Boolean flag = miniInfoService.bindOpenInfo(tenantId);
        return Result.ok(flag);
    }


    @GetMapping("revocation")
    @ApiOperation(value = "撤销小程序当前审核中的版本")
    public Result revocation() {
        miniInfoService.revocation();
        return Result.ok();
    }


    @GetMapping("version/update")
    @ApiOperation(value = "小程序版本更新")
    public Result versionUpdate(@ApiParam(value = "店铺模版详情小程序版本子表id", required = true)@RequestParam Long templateDetailMinisId) {
        miniInfoService.versionUpdate(templateDetailMinisId);
        return Result.ok();
    }

    @GetMapping("base/info/update")
    @ApiOperation(value = "更新小程序基本信息")
    public Result baseInfoUpdate() {
        miniInfoService.baseInfoUpdate();
        return Result.ok();
    }

    @PostMapping("mini/req")
    @ApiOperation(value = "手动触发重新上传小程序域名配置")
    public Result miniReq() {
        miniInfoService.miniDomaiReq();
        return Result.ok();
    }

}
