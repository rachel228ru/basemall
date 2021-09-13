package com.medusa.gruul.platform.web.controller;

import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.platform.model.vo.PublicShopInfoVo;
import com.medusa.gruul.platform.service.PublicServiceApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author whh
 * @description 对外开放公众接口
 * @data: 2020/7/18
 */
@Api(tags = "对外开放公众接口")
@RequestMapping("/public-service")
@RestController
public class PublicServiceApiController {

    @Autowired
    private PublicServiceApiService publicServiceApiService;


    @GetMapping("/get/appId")
    @EscapeLogin
    @ApiOperation(value = "根据租户id换取小程序部分开放信息")
    public Result<PublicShopInfoVo> publicShopInfo(@RequestParam(name = "tenantId") String tenantId) {
        PublicShopInfoVo shopInfoVo = publicServiceApiService.getPublicShopInfo(tenantId);
        return Result.ok(shopInfoVo);
    }

}
