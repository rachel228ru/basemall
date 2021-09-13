package com.medusa.gruul.platform.web.controller;


import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.common.dto.SendCodeVerifyDto;
import com.medusa.gruul.platform.model.dto.SendCodeDto;
import com.medusa.gruul.platform.service.ISendCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author whh
 * @since 2019-10-07
 */
@RestController(value = "sendCode")
@RequestMapping("/send-code")
@Api(tags = "短信接口")
public class SendCodeController {

    @Autowired
    private ISendCodeService sendCodeService;

    @PostMapping
    @EscapeLogin
    @ApiOperation(value = "发送一次性验证,后端在common中定义校验类型,有效期统一5分钟,使用即失效,60秒可发送一次", tags = "代理相关")
    public Result sendCode(@RequestBody SendCodeDto sendCodeDto) {
        sendCodeService.sendCode(sendCodeDto);
        return Result.ok();
    }


    @PutMapping("/verify/code")
    @EscapeLogin
    @ApiOperation(value = "验证码校验,校验成功返回凭证码", tags = "代理相关")
    public Result<String> verifyCode(@RequestBody SendCodeVerifyDto sendCodeVerifyDto) {
        String certificate = sendCodeService.verifyCode(sendCodeVerifyDto);
        return Result.ok(certificate);
    }


}
