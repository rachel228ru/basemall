package com.medusa.gruul.platform.web.controller;


import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.common.dto.HeartBeatDto;
import com.medusa.gruul.platform.api.model.dto.OssConfigDto;
import com.medusa.gruul.platform.model.dto.CodeEmailConfDto;
import com.medusa.gruul.platform.model.dto.KfAddDto;
import com.medusa.gruul.platform.model.dto.SaveConfigDto;
import com.medusa.gruul.platform.model.dto.SavePayConfigDto;
import com.medusa.gruul.platform.model.vo.CodeEmailConfVo;
import com.medusa.gruul.platform.model.vo.KfmsgVo;
import com.medusa.gruul.platform.model.vo.PayConfigVo;
import com.medusa.gruul.platform.model.vo.SystemConfigVo;
import com.medusa.gruul.platform.service.ISystemConfService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 系统配置 前端控制器
 * </p>
 *
 * @author whh
 * @since 2019-09-20
 */
@Api(tags = "系统配置")
@RestController
@RequestMapping("/system-conf")
public class SystemConfController {

    @Autowired
    private ISystemConfService systemConfService;

    @PostMapping
    @EscapeLogin
    @ApiOperation(value = "保存配置或更新服务器配置")
    public Result add(@RequestBody @Valid SaveConfigDto saveConfigDto) {
        systemConfService.saveValue(saveConfigDto);
        return Result.ok();
    }

    @GetMapping("/service/config")
    @EscapeLogin
    @ApiOperation(value = "查询服务器配置")
    public Result<SystemConfigVo> getTypeInfo() {
        SystemConfigVo systemConfigVo = systemConfService.getTypeInfo(CommonConstants.NUMBER_ZERO);
        return Result.ok(systemConfigVo);
    }

    @GetMapping("/current/oss/config")
    @EscapeLogin
    @ApiOperation(value = "获取当前平台使用oss配置")
    public Result<OssConfigDto> ossConfig(@ApiParam(value = "查询类型 0-当前使用的配置 1：七牛  2：阿里云  3：腾讯云", required = true)
                                          @RequestParam(name = "type") Integer type) {
        return systemConfService.ossConfig(type);
    }


    @PostMapping("/heart/beat")
    @EscapeLogin
    @ApiOperation(value = "心跳配置")
    public Result heartBeat(@RequestBody @Valid HeartBeatDto heartBeatDto) {
        System.out.println(heartBeatDto.toString());
        return Result.ok();
    }


    @PostMapping("/pay/config")
    @EscapeLogin
    @ApiOperation(value = "保存或更新收款配置")
    public Result savePayConfig(@RequestBody SavePayConfigDto savePayConfigDto) {
        systemConfService.savePayConfig(savePayConfigDto);
        return Result.ok();
    }

    @GetMapping("/pay/config")
    @EscapeLogin
    @ApiOperation(value = "查询收款配置")
    public Result<PayConfigVo> getPayConfig() {
        PayConfigVo payConfigVo = systemConfService.getPayConfig();
        return Result.ok(payConfigVo);
    }

    @GetMapping("/kf/msg")
    @EscapeLogin
    @ApiOperation(value = "查询消息配置")
    public Result<KfmsgVo> getKfmsg() {
        KfmsgVo vo = systemConfService.getKfmsg();
        return Result.ok(vo);
    }

    @PostMapping("/kf/msg")
    @EscapeLogin
    @ApiOperation(value = "保存或更新客服消息配置")
    public Result saveKfmsg(@RequestBody KfAddDto dto) {
        systemConfService.saveKfmsg(dto);
        return Result.ok();
    }


    @PostMapping("/code/email")
    @EscapeLogin
    @ApiOperation(value = "保存或更新短信邮箱配置")
    public Result codeEmailConf(@RequestBody CodeEmailConfDto emailConfDto) {
        systemConfService.saveCodeEmailConf(emailConfDto);
        return Result.ok();
    }

    @GetMapping("/code/email")
    @EscapeLogin
    @ApiOperation(value = "查询短信邮箱配置")
    public Result<CodeEmailConfVo> getCodeEmailConf() {
        CodeEmailConfVo vo = systemConfService.getCodeEmailConf();
        return Result.ok(vo);
    }
}
