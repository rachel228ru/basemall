package com.medusa.gruul.platform.web.remote;

import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.platform.api.entity.MiniInfo;
import com.medusa.gruul.platform.api.model.dto.*;
import com.medusa.gruul.platform.api.model.vo.MiniMsgVo;
import com.medusa.gruul.platform.api.model.vo.PayInfoVo;
import com.medusa.gruul.platform.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author whh
 */
@RestController(value = "remoteMiniInfoController")
@RequestMapping("/")
@Api(tags = "远程调用接口,仅限后端fegin调用-->RemoteMiniInfoService")
public class RemoteMiniInfoController {

    @Autowired
    private IMiniInfoService miniInfoService;
    @Autowired
    private IDefaultValueService defaultValueService;
    @Autowired
    private IAccountInfoService accountInfoService;
    @Autowired
    private ISystemConfService systemConfService;
    @Autowired
    private IPlatformShopMessageService platformShopMessageService;
    @Autowired
    private IPlatformShopInfoService platformShopInfoService;


    /**
     * 获取指定租户id店铺信息
     *
     * @return com.medusa.gruul.platform.api.model.dto.ShopInfoDto
     */
    @GetMapping(value = "/get/shop/info")
    @EscapeLogin
    @ApiOperation(value = "获取指定租户id店铺信息")
    public Result<ShopInfoDto> getShopInfo(@RequestParam(value = "tenantId", required = true) String tenantId) {
        return platformShopInfoService.getShopInfo(tenantId);
    }


    @GetMapping(value = "/mini/subscribe/msg")
    @EscapeLogin
    @ApiOperation(value = "获取当前店铺可使用的小程序订阅模板")
    public List<MiniMsgVo> getCurrentMiniMsg() {
        return platformShopMessageService.getCurrentMiniMsg();
    }

    @GetMapping("/affirm/lessee/{token}")
    @ApiOperation(value = "确认是否商家")
    @EscapeLogin
    public Result<Boolean> affirmLessee(@PathVariable(value = "token") String token) {
        return Result.ok(accountInfoService.affirmLessee(token));
    }

    @GetMapping("/oss/config")
    @EscapeLogin
    @ApiOperation(value = "获取当前平台使用oss配置")
    public Result<OssConfigDto> currentOssConfig() {
        return systemConfService.currentOssConfig();
    }


    @GetMapping("/login/{tenantId}/{code}")
    @EscapeLogin
    @ApiOperation(value = "提供小程序第三方登录")
    public LoginDto login(@PathVariable(value = "tenantId", required = true) String tenantId,
                          @PathVariable(value = "code", required = true) String code) {
        return miniInfoService.login(tenantId, code);
    }

    @GetMapping("/mini/auth/info")
    @EscapeLogin
    @ApiOperation(value = "根据租户id换取指定小程序授权信息")
    public Result<MiniAuthInfoDto> getMiniAuthInfo(@RequestParam(value = "tenantId", required = true) String tenantId) {
        return miniInfoService.getMiniAuthInfo(tenantId);
    }

    @GetMapping("/default/value")
    @EscapeLogin
    @ApiOperation(value = "提供默认值查询")
    public String getDefaultValue(@RequestParam(value = "version", required = true) String version,
                                  @RequestParam(value = "uniqueIdentification", required = true) String uniqueIdentification) {
        return defaultValueService.getDefaultValue(version, uniqueIdentification);
    }


    @GetMapping("/shop/config")
    @EscapeLogin
    @ApiOperation(value = "根据租户id获取店铺配置信息(小程序信息,支付配置)")
    public ShopConfigDto getShopConfig(@RequestParam(value = "tenantId", required = true) String tenantId) {
        return platformShopInfoService.getShopConfig(tenantId);
    }

    @GetMapping("/shop/config/appid")
    @EscapeLogin
    @ApiOperation(value = "根据appid获取店铺配置信息(小程序信息,支付配置)")
    public ShopConfigDto getShopConfigAndAppId(@RequestParam(value = "appId", required = true) String appId) {
        return platformShopInfoService.getShopConfigAndAppId(appId);
    }

    /**
     * 根据租户id获取店铺当前使用的套餐功能状态
     * <p>
     * code == 200 返回正确数据
     *
     * @return com.medusa.gruul.platform.api.model.dto.ShopInfoDto
     */
    @GetMapping(value = "/get/shop/function")
    @EscapeLogin
    @ApiOperation(value = "根据租户id获取店铺当前使用的套餐功能状态,当前仅限拼团模板数据")
    public Result<ShopPackageFunctionDto> getShopFunction(@RequestParam(value = "tenantId", required = true) String tenantId) {
        return platformShopInfoService.getShopFunction(tenantId);
    }

    @GetMapping(value = "/shops/all")
    @EscapeLogin
    @ApiOperation(value = "获取当前所有租户id")
    public List<String> getShopsAll(){
        return platformShopInfoService.getShopsAll();
    }
}
