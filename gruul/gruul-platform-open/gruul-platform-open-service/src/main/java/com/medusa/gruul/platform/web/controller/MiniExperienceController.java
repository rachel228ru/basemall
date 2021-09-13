package com.medusa.gruul.platform.web.controller;


import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.platform.model.dto.MiniTesterDto;
import com.medusa.gruul.platform.service.IMiniExperienceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 小程序体验者表 前端控制器
 * </p>
 *
 * @author whh
 * @since 2020-01-14
 */
@RestController
@RequestMapping("/mini-experience")
@Api(tags = "小程序相关接口")
public class MiniExperienceController {

    @Autowired
    private IMiniExperienceService miniExperienceService;


    @GetMapping("wxa/get_qrcode")
    @ApiOperation(value = "获取的体验版二维码,返回体验二维码base64")
    public Result wxaGetQrcode(@RequestParam(required = false) @ApiParam(value = "指定二维码扫码后直接进入指定页面并可同时带上参数）,非必须", required = false) String path) {
        return miniExperienceService.wxaGetQrcode(path);
    }

    @GetMapping("wxa/memberauth")
    @ApiOperation(value = "获取体验者列表,返回人员对应的唯一字符串")
    public Result<List<String>> wxaMemberauth() {
        List<String> list = miniExperienceService.wxaMemberauth();
        return Result.ok(list);
    }

    @PostMapping("wxa/tester")
    @ApiOperation(value = "解除绑定或绑定体验者")
    public Result tester(@RequestBody @Validated MiniTesterDto miniTesterDto) {
        miniExperienceService.tester(miniTesterDto);
        return Result.ok();
    }
}
