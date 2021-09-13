package com.medusa.gruul.account.web.controller;


import com.medusa.gruul.account.model.dto.UpdateUserExtendsInfoDto;
import com.medusa.gruul.account.service.IMiniAccountExtendsService;
import com.medusa.gruul.common.core.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户信息扩展表 前端控制器
 * </p>
 *
 * @author whh
 * @since 2019-11-18
 */
@RestController
@Api("用户扩展字段相关")
@RequestMapping("/mini-account-extends")
public class MiniAccountExtendsController {


    @Autowired
    private IMiniAccountExtendsService miniAccountExtendsService;

    @PostMapping
    @ApiOperation(value = "更新用户扩展信息部分字段")
    public Result updateUserExtendsInfo(@RequestBody UpdateUserExtendsInfoDto updateUserExtendsInfoDto) {
        miniAccountExtendsService.updateUserExtendsInfo(updateUserExtendsInfoDto);
        return Result.ok();
    }

}
