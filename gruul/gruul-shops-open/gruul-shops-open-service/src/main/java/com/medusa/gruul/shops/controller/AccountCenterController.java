package com.medusa.gruul.shops.controller;


import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.shops.api.model.AccountCenterSettingDto;
import com.medusa.gruul.shops.api.model.AccountCenterVo;
import com.medusa.gruul.shops.service.IAccountCenterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户中心配置 前端控制器
 * </p>
 *
 * @author whh
 * @since 2019-11-18
 */
@RestController
@RequestMapping("/account-center")
@Api(tags = "用户中心")
public class AccountCenterController {
    @Autowired
    private IAccountCenterService accountCenterService;

    @PutMapping
    @ApiOperation(value = "修改用户中心配置")
    public Result accountCenterSettingMotify(@RequestBody AccountCenterSettingDto dto) {
        accountCenterService.accountCenterSettingMotify(dto);
        return Result.ok();
    }

    @GetMapping
    @ApiOperation(value = "获取用户中心配置")
    public Result<AccountCenterVo> accountCenterSetting() {
        AccountCenterVo vo = accountCenterService.accountCenterSetting();
        return Result.ok(vo);
    }
}
