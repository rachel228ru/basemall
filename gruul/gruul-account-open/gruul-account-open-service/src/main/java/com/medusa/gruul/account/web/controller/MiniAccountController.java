package com.medusa.gruul.account.web.controller;


import com.medusa.gruul.account.model.dto.DecodePhoneInfo;
import com.medusa.gruul.account.model.dto.SetBlacklistDto;
import com.medusa.gruul.account.model.dto.UpdateUserBaseInfoDto;
import com.medusa.gruul.account.model.vo.BlacklistUserVo;
import com.medusa.gruul.account.model.vo.LoginBaseInfoVo;
import com.medusa.gruul.account.model.vo.UserInfoVo;
import com.medusa.gruul.account.model.vo.UserListVo;
import com.medusa.gruul.account.service.IMiniAccountRestrictService;
import com.medusa.gruul.account.service.IMiniAccountService;
import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.common.core.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 小程序用户表 前端控制器
 * </p>
 *
 * @author whh
 * @since 2019-11-18
 */
@RestController
@RequestMapping("/mini-account")
@Api(tags = "用户相关接口")
public class MiniAccountController {

    @Autowired
    private IMiniAccountService miniAccountService;
    @Autowired
    private IMiniAccountRestrictService miniAccountRestrictService;

    @GetMapping("/login")
    @ApiOperation(value = "小程序端登录")
    @EscapeLogin
    public Result<LoginBaseInfoVo> login(@ApiParam(value = "code", required = true) @RequestParam String code) {
        LoginBaseInfoVo vo = miniAccountService.login(code);
        return Result.ok(vo);
    }

    @PostMapping("/decode/phone/info")
    @ApiOperation(value = "解密获取用户手机号")
    public Result decodePhoneInfo(@RequestBody @Validated DecodePhoneInfo decodePhoneInfo) {
        String phone = miniAccountService.decodePhoneInfo(decodePhoneInfo);
        return Result.ok(phone);
    }

    @PostMapping
    @ApiOperation(value = "更新用户基础信息")
    public Result updateUserBaseInfo(@RequestBody @Validated UpdateUserBaseInfoDto updateUserBaseInfoDto) {
        miniAccountService.updateUserBaseInfo(updateUserBaseInfoDto);
        return Result.ok();
    }

    @GetMapping
    @ApiOperation(value = "根据token获取指定用户基本信息", notes = "小程序接口")
    public Result<UserInfoVo> userInfo(@ApiParam(value = "数据级别 1-基础信息 2-用户扩展数据 3-用户限制数据 未传默认未1", required = true) @RequestParam(value = "infoLevel", required = false, defaultValue = "1") Integer infoLevel) {
        UserInfoVo vo = miniAccountService.getUserInfo(infoLevel);
        return Result.ok(vo);
    }

    @GetMapping(value = "/qr_code")
    @ApiOperation(value = "获取当前用户二维码")
    public Result<String> qrCode() {
        String qrCode = miniAccountService.qrCode();
        return Result.ok(qrCode);
    }


    @PutMapping(value = "switch/shops/{shopId}")
    @ApiOperation(value = "切换店铺,调用之后会返回新的token,同时需重新调用获取指定用户基本信息", notes = "店铺相关接口")
    public Result<String> switchShops(@ApiParam(value = "店铺id", required = true) @PathVariable(value = "shopId") String shopId) {
        String token = miniAccountService.switchShops(shopId);
        return Result.ok(token);
    }

    @GetMapping("list")
    @ApiOperation(value = "pc端获取用户列表")
    public Result<PageUtils<List<UserListVo>>> userList(
            @ApiParam(value = "指定页数", required = true) @RequestParam Integer page,
            @ApiParam(value = "数据条数", required = true) @RequestParam Integer size,
            @ApiParam(name = "nikeName", value = "微信昵称") @RequestParam(required = false) String nikeName,
            @ApiParam(name = "becomeMemberStartTime", value = "成为会员开始时间 2019-11-11 11:23:23") @RequestParam(required = false) String becomeMemberStartTime,
            @ApiParam(name = "becomeMemberEndTime", value = "成为会员结束时间 2019-11-11 11:23:23") @RequestParam(required = false) String becomeMemberEndTime,
            @ApiParam(name = "tagId", value = "标签id") @RequestParam(required = false) Long tagId,
            @ApiParam(name = "orderSuccessStartTime", value = "成交时间 2019-11-11 11:23:23") @RequestParam(required = false) String orderSuccessStartTime,
            @ApiParam(name = "orderSuccessEndTime", value = "成交时间 2019-11-11 11:23:23") @RequestParam(required = false) String orderSuccessEndTime,
            @ApiParam(name = "memberNumber", value = "会员编号") @RequestParam(required = false) String memberNumber,
            @ApiParam(name = "sort", value = "排序类型 1-交易总额降序 2-交易总额升序 3-会员时间降序 4-会员时间升序") @RequestParam(required = false) Integer sortType
    ) {
        PageUtils<List<UserListVo>> voPageUtils = miniAccountService.userList(nikeName, becomeMemberStartTime, becomeMemberEndTime,
                tagId, orderSuccessStartTime, orderSuccessEndTime,  memberNumber, page, size, sortType);
        return Result.ok(voPageUtils);
    }

    @GetMapping("/blacklist")
    @ApiOperation(value = "获取黑名单用户列表")
    public Result<PageUtils<List<BlacklistUserVo>>> blacklist(
            @ApiParam(value = "指定页数", required = true) @RequestParam Integer page,
            @ApiParam(value = "数据条数", required = true) @RequestParam Integer size,
            @ApiParam(value = "权限 1-下单 2-评论") @RequestParam(value = "permission", required = false) Integer permission,
            @ApiParam(value = "手机号或微信昵称模糊搜索") @RequestParam(value = "fuzzy", required = false) String fuzzy) {
        PageUtils<List<BlacklistUserVo>> pageUtils = miniAccountService.blacklist(page, size, permission, fuzzy);
        return Result.ok(pageUtils);
    }

    @PutMapping("/set/blacklist")
    @ApiOperation(value = "批量设置黑名单用户")
    public Result setBlacklist(@RequestBody SetBlacklistDto dto) {
        miniAccountRestrictService.setBlacklist(dto);
        return Result.ok();
    }

}
