package com.medusa.gruul.account.web.controller;


import com.medusa.gruul.account.model.dto.UserTagDto;
import com.medusa.gruul.account.model.vo.TagVo;
import com.medusa.gruul.account.service.IMiniAccountTagService;
import com.medusa.gruul.common.core.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户标签表 前端控制器
 * </p>
 *
 * @author whh
 * @since 2019-11-18
 */
@RestController
@RequestMapping("/mini-account-tag")
@Api(tags = "标签接口")
public class MiniAccountTagController {


    @Autowired
    private IMiniAccountTagService miniAccountTagService;

    @GetMapping
    @ApiOperation(value = "查询所有标签")
    public Result<List<TagVo>> listAll() {
        List<TagVo> tagVos = miniAccountTagService.listAll();
        return Result.ok(tagVos);
    }

    @PostMapping
    @ApiOperation(value = "设置用户标签(添加|删除|设置)")
    public Result settingUserTag(@RequestBody UserTagDto userTagDto) {
        miniAccountTagService.settingUserTag(userTagDto);
        return Result.ok();
    }

}
