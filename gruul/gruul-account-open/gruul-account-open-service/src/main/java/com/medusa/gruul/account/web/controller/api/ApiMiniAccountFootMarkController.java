package com.medusa.gruul.account.web.controller.api;

import com.medusa.gruul.account.model.dto.AccountFootMarkDto;
import com.medusa.gruul.account.model.vo.AccountFootMarkParam;
import com.medusa.gruul.account.service.IApiMiniAccountFootMarkService;
import com.medusa.gruul.common.core.util.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * @author xiaoq
 * 小程序用戶足迹 前端控制器
 * @data 2020/2/28 10:34
 */
@RestController
@RequestMapping("/api/FootMark")
public class ApiMiniAccountFootMarkController {

    @Autowired
    private IApiMiniAccountFootMarkService miniAccountFootMarkService;

    /**
     * 获取足迹信息
     *
     * @param accountFootMarkParam 分页数据
     * @return 足迹信息
     */
    @GetMapping("/getUserFootMarkInfo")
    @ApiOperation(value = "获取用户足迹信息")
    public Result accountFootMarkList(AccountFootMarkParam accountFootMarkParam) {
        List<String> accountFootMark = miniAccountFootMarkService.getAccountFootMark(accountFootMarkParam);
        return Result.ok(accountFootMark);
    }


    /**
     * 添加足迹信息
     *
     * @param accountFootMarkDto
     * @return
     */
    @PostMapping("/add/FootMarkInfo")
    @ApiOperation(value = "添加用户足迹信息")
    public Result addAccountFootMark(@RequestBody @Validated AccountFootMarkDto accountFootMarkDto) {
        miniAccountFootMarkService.addAccountFootMark(accountFootMarkDto);
        return Result.ok();
    }


    /**
     * 删除足迹信息
     *
     * @param footmarkIds 足迹ids
     * @param type        :0-删除, 1-清空
     * @return
     */
    @PutMapping("/del/FootMarkInfo/{type}")
    @ApiOperation(value = "删除用户足迹信息")
    public Result deleteAccountFootMark(@RequestBody Long[] footmarkIds,
                                        @PathVariable(value = "type") @NotNull Integer type) {
        System.out.println(type);
        miniAccountFootMarkService.delAccountFootMark(Arrays.asList(footmarkIds), type);
        return Result.ok();
    }

    /**
     * 获取用户足迹数量
     *
     * @return
     */
    @GetMapping("/getAccountFootMarkCount")
    @ApiOperation(value = "获取用户足迹数量")
    public Result getAccountFootMarkCount() {
        int accountFootMarkCount = miniAccountFootMarkService.getAccountFootMarkCount();
        return Result.ok(accountFootMarkCount);
    }
}
