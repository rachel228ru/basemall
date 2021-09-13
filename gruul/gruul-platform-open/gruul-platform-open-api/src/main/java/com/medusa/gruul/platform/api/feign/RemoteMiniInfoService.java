package com.medusa.gruul.platform.api.feign;

import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.platform.api.entity.MiniInfo;
import com.medusa.gruul.platform.api.model.dto.*;
import com.medusa.gruul.platform.api.model.vo.MiniMsgVo;
import com.medusa.gruul.platform.api.model.vo.PayInfoVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author whh
 */
@FeignClient(value = "platform-open")
public interface RemoteMiniInfoService {


    /**
     * 获取指定租户id店铺信息
     *
     * @param tenantId 租户id
     * @return com.medusa.gruul.platform.api.model.dto.ShopInfoDto
     */
    @RequestMapping(value = "/get/shop/info", method = RequestMethod.GET)
    @ApiOperation(value = "获取指定租户id店铺信息")
    Result<ShopInfoDto> getShopInfo(@RequestParam(value = "tenantId") String tenantId);


    /**
     * 获取当前店铺可使用的小程序订阅模板
     *
     * @return com.medusa.gruul.platform.api.model.vo.MiniMsgVo
     */
    @RequestMapping(value = "/mini/subscribe/msg", method = RequestMethod.GET)
    @ApiOperation(value = "获取当前店铺可使用的小程序订阅模板")
    List<MiniMsgVo> getCurrentMiniMsg();

    /**
     * 确认token和租户id是否一致,确认是否商家账号
     *
     * @param token 请求token
     * @return java.lang.Boolean
     */
    @RequestMapping(value = "/affirm/lessee/{token}", method = RequestMethod.GET)
    @ApiOperation(value = "确认是否商家,返回token和租户id是否一致 确认是否商家账号")
    Result<Boolean> affirmLessee(@PathVariable(value = "token") String token);

    /**
     * 获取当前平台使用oss配置(默认使用七牛云)
     *
     * @return com.medusa.gruul.platform.api.model.dto.OssConfigDto
     */
    @RequestMapping(value = "/oss/config", method = RequestMethod.GET)
    @ApiOperation(value = "获取当前平台使用oss配置")
    Result<OssConfigDto> currentOssConfig();

    /**
     * 根据code换取小程序用户基本信息
     *
     * @param tenantId 租户标识
     * @param code     code
     * @return com.medusa.gruul.platform.api.entity.MiniInfo
     */
    @ApiOperation(value = "根据code换取小程序用户基本信息")
    @RequestMapping(value = "/login/{tenantId}/{code}", method = RequestMethod.GET)
    LoginDto login(@PathVariable(value = "tenantId") String tenantId,
                   @PathVariable(value = "code") String code);

    /**
     * 根据租户id换取指定小程序信息
     *
     * @param tenantId 租户id
     * @return com.medusa.gruul.platform.api.model.dto.MiniAuthInfoDto
     * code=200 返回所需信息;  code=400返回错误原因
     */
    @GetMapping("/mini/auth/info")
    @ApiOperation(value = "根据租户id换取小程序部分授权信息")
    Result<MiniAuthInfoDto> getMiniAuthInfo(@RequestParam(value = "tenantId") String tenantId);

    /**
     * 提供默认值查询
     *
     * @param version              租户标识
     * @param uniqueIdentification 唯一标识
     * @return 导入时的kv
     */
    @ApiOperation(value = "提供默认值查询")
    @RequestMapping(value = "/default/value", method = RequestMethod.GET)
    String getDefaultValue(@RequestParam(value = "version") String version,
                           @RequestParam(value = "uniqueIdentification") String uniqueIdentification);


    /**
     * 获取店铺配置信息
     *
     * @param tenantId 租户id
     * @return com.medusa.gruul.platform.api.model.dto.ShopConfigDto
     */
    @RequestMapping(value = "/shop/config", method = RequestMethod.GET)
    @ApiOperation(value = "根据租户id获取店铺配置信息")
    ShopConfigDto getShopConfig(@RequestParam(value = "tenantId") String tenantId);


    /**
     * 根据appid获取店铺配置信息
     *
     * @param appId 小程序appId
     * @return com.medusa.gruul.platform.api.model.dto.ShopConfigDto
     */
    @RequestMapping(value = "/shop/config/appid", method = RequestMethod.GET)
    @ApiOperation(value = "根据appid获取店铺配置信息(小程序信息,支付配置)")
    ShopConfigDto getShopConfigAndAppId(@RequestParam(value = "appId", required = true) String appId);


    /**
     * 根据租户id获取店铺当前使用的套餐功能状态
     * <p>
     * code == 200 返回正确数据
     *
     * @param tenantId 租户id
     * @return com.medusa.gruul.platform.api.model.dto.ShopInfoDto
     */
    @RequestMapping(value = "/get/shop/function", method = RequestMethod.GET)
    @ApiOperation(value = "根据租户id获取店铺当前使用的套餐功能状态,当前仅限拼团模板数据")
    Result<ShopPackageFunctionDto> getShopFunction(@RequestParam(value = "tenantId") String tenantId);

    /**
     * 获取当前所有的租户id列表
     * 仅限默认数据生成兼容使用，调用方请勿频繁调用
     * @return List<String>
     */
    @RequestMapping(value = "/shops/all", method = RequestMethod.GET)
    @ApiOperation(value = "获取当前所有的租户id列表，仅限默认数据生成兼容使用，调用方请勿频繁调用")
    List<String> getShopsAll();

}
