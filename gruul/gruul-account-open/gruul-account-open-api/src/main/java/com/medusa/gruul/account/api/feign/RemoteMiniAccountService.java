package com.medusa.gruul.account.api.feign;

import com.medusa.gruul.account.api.model.*;
import com.medusa.gruul.common.core.util.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * @author whh
 * @date 2019/11/06
 */
@FeignClient(value = "account-open")
public interface RemoteMiniAccountService {


    /**
     * 修改用户扩展字段部分数据
     *
     * @param userId                      用户id
     * @param miniAccountExtendsUpdateDto com.medusa.gruul.account.api.model.MiniAccountExtendsUpdateDto
     * @return java.lang.Boolean  修改成功->true   失败->false
     */
    @RequestMapping(value = "/portion/attribute/modify/{userId}", method = RequestMethod.POST)
    @ApiOperation(value = "修改用户扩展字段部分数据")
    Boolean portionAttributeModify(@PathVariable(value = "userId", required = true) @NotNull(message = "用户id不能为null") String userId,
                                   @RequestBody @NotNull(message = "修改数据不能为null") MiniAccountExtendsUpdateDto miniAccountExtendsUpdateDto);

    /**
     * 获取用户信息接口
     *
     * @param userId 用户id
     * @param infos  [1,2,3,4]  1,基本信息,2,扩展信息,3-地址信息,4-授权信息  需要哪些发哪些 list
     * @return com.medusa.gruul.account.api.model.AccountInfoDto
     */
    @RequestMapping(value = "/account/info/{userId}", method = RequestMethod.GET)
    @ApiOperation(value = "获取用户信息,注意判空")
    AccountInfoDto accountInfo(@PathVariable(value = "userId", required = true) @NotNull(message = "用户id不能为null") String userId,
                               @RequestParam(value = "infos", required = true) @NotNull(message = "获取数据数组不能为空") List<Integer> infos);

    /**
     * 批量获取指定用户id(用户店铺id)的用户基本信息
     *
     * @param shopUserId 用户id数组
     * @return com.medusa.gruul.account.api.model.AccountInfoDto
     */
    @RequestMapping(value = "/accounts/info", method = RequestMethod.GET)
    @ApiOperation(value = "批量获取指定用户id(用户店铺id)的用户基本信息")
    List<MiniAccountExtDto> accountsInfoList(@RequestParam(value = "shopUserId") @NotNull(message = "用户id不能为null") List<String> shopUserId);

    /**
     * 公众号授权登录,返回登录token
     *
     * @param wxMpUserDto com.medusa.gruul.platform.api.model.dto.WxMpUserDto
     * @return java.lang.String
     */
    @RequestMapping(value = "/mp/login", method = RequestMethod.POST)
    @ApiOperation(value = "公众号授权登录")
    Result mpLogin(WxMpUserDto wxMpUserDto);

}
