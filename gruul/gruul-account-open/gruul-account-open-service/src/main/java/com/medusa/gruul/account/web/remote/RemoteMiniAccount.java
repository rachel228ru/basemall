package com.medusa.gruul.account.web.remote;

import com.medusa.gruul.account.api.model.*;
import com.medusa.gruul.account.service.*;
import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.common.core.util.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author whh
 * @description
 * @data: 2019/11/29
 */
@RestController(value = "remoteMiniAccount")
public class RemoteMiniAccount {

    @Autowired
    private IMiniAccountService miniAccountService;
    @Autowired
    private IMiniAccountExtendsService miniAccountExtendsService;

    /**
     * 修改用户扩展字段部分数据
     *
     * @param userId                      用户id
     * @param miniAccountExtendsUpdateDto com.medusa.gruul.account.api.model.MiniAccountExtendsUpdateDto
     * @return java.lang.Boolean
     */
    @RequestMapping(value = "/portion/attribute/modify/{userId}", method = RequestMethod.POST)
    @ApiOperation(value = "修改用户扩展字段部分数据")
    @EscapeLogin
    public Boolean portionAttributeModify(@PathVariable(value = "userId", required = true) @NotNull(message = "用户id不能为null") String userId,
                                          @RequestBody @NotNull(message = "修改数据不能为null") MiniAccountExtendsUpdateDto miniAccountExtendsUpdateDto) {
        return miniAccountExtendsService.portionAttributeModify(userId, miniAccountExtendsUpdateDto);
    }


    /**
     * 获取用户信息接口
     *
     * @param shopUserId 用户id
     * @param infos      [1,2,3,4]  1,基本信息,2,扩展信息,3-地址信息,4-授权信息  需要哪些发哪些 list
     * @return com.medusa.gruul.account.api.model.AccountInfoDto
     */
    @RequestMapping(value = "/account/info/{userId}", method = RequestMethod.GET)
    @ApiOperation(value = "获取用户信息,注意判空")
    @EscapeLogin
    public AccountInfoDto accountInfo(@PathVariable(value = "userId", required = true) @NotNull(message = "用户id不能为null") String shopUserId,
                                      @RequestParam(value = "infos", required = true) @NotNull(message = "获取数据数组不能为空") List<Integer> infos) {
        return miniAccountService.accountInfo(shopUserId, infos);
    }




    /**
     * 批量获取指定用户id(用户店铺id)的用户基本信息
     *
     * @param shopUserId 用户id数组
     * @return com.medusa.gruul.account.api.model.AccountInfoDto
     */
    @EscapeLogin
    @RequestMapping(value = "/accounts/info", method = RequestMethod.GET)
    @ApiOperation(value = "批量获取指定用户id(用户店铺id)的用户基本信息")
    public List<MiniAccountExtDto> accountsInfoList(
            @RequestParam(value = "shopUserId", required = true) @NotNull(message = "店铺用户id不能为null") List<String> shopUserId) {
        return miniAccountService.accountsInfoList(shopUserId);
    }


    /**
     * 积分确认是否足够或直接确认减少接口
     *
     * @param userId   用户id
     * @param value    要减少的积分值
     * @param isDeduct 是否直接减少 true 直接减少  false不操作积分,只确认积分是否足够
     * @return 情况1:  isDeduct == true && 减少积分足够 return true   直接扣减并且积分足够
     * 情况2:  isDeduct == false && 减少积分足够 return true  不扣减仅确认积分是否足够
     */
    @RequestMapping(value = "/integral/subtract/affirm/{userId}", method = RequestMethod.GET)
    @ApiOperation(value = "积分减少确认接口")
    @EscapeLogin
    public Boolean integralSubtractAffirm(@PathVariable(value = "userId", required = true) @NotNull(message = "用户id不能为null") String userId,
                                          @RequestParam(value = "value", required = true) @NotNull(message = "积分值不能为空") BigDecimal value,
                                          @RequestParam(value = "isDeduct", required = true) @NotNull(message = "是否直接扣除字段不能为null") Boolean isDeduct) {
        //Todo 积分
        return null;
    }

    /**
     * 公众号授权登录,返回登录token
     *
     * @param wxMpUserDto com.medusa.gruul.platform.api.model.dto.WxMpUserDto
     * @return java.lang.String
     */
    @RequestMapping(value = "/mp/login", method = RequestMethod.POST)
    @ApiOperation(value = "公众号授权登录")
    @EscapeLogin
    public Result mpLogin(@RequestBody WxMpUserDto wxMpUserDto) {
        return miniAccountService.mpLogin(wxMpUserDto);
    }

}
