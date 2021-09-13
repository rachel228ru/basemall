package com.medusa.gruul.platform.web.controller;

import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.platform.model.dto.PreAuthCodeDto;
import com.medusa.gruul.platform.service.IMiniInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 小程序相关接口
 * </p>
 *
 * @author whh
 * @since 2019-09-07
 */
@RestController
@RequestMapping("/mini/authorization")
@Api(tags = "小程序授权相关接口")
public class MiniAuthorizationController {

    @Autowired
    private IMiniInfoService miniInfoService;

    @PostMapping("/preauthcode")
    @EscapeLogin
    @ApiOperation(value = "获取扫码授权url,需存在正确token值")
    public Result<String> getPreAuthCode(@RequestBody @Validated PreAuthCodeDto preAuthCodeDto) {
        String preAuthCodeUrl = miniInfoService.getPreAuthCode(preAuthCodeDto);
        return Result.ok(preAuthCodeUrl);
    }

    /**
     * @param authCode  授权码
     * @param expiresIn 有效时间
     * @param uuid      获取扫码授权url时设置的uuld
     * @param response  resp
     */
    @GetMapping("/preauthcode/notify/{UUID}")
    @EscapeLogin
    @ApiOperation(value = "新增授权回调地址,第三方调用", hidden = true)
    public void preAuthCodeNotify(@RequestParam(name = "auth_code") String authCode, @RequestParam(name = "expires_in") Integer expiresIn,
                                  @PathVariable(name = "UUID") String uuid, HttpServletResponse response) {
        miniInfoService.preAuthCodeNotify(authCode, expiresIn, uuid, response);
    }


    /**
     * @param platformShopId 平台店铺id
     */
    @GetMapping("/error")
    @ApiOperation(value = "根据平台店铺id查询当前授权信息,每次授权消息最多保留5分钟")
    public Result<String> errorInfo(@ApiParam(value = "平台店铺id") @RequestParam Long platformShopId) {
        return miniInfoService.errorInfo(platformShopId);
    }

}
