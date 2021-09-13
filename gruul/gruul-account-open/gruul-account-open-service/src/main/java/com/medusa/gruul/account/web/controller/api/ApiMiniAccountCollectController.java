package com.medusa.gruul.account.web.controller.api;

import com.medusa.gruul.account.model.dto.UserCollectDto;
import com.medusa.gruul.account.model.vo.UserCollectVo;
import com.medusa.gruul.account.service.IApiMiniAccountCollectService;
import com.medusa.gruul.common.core.util.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xiaoq
 * 小程序用戶收藏 前端控制器
 * @data 2020/2/22 15:32
 */
@RestController
@RequestMapping("/api/collect")
public class ApiMiniAccountCollectController {

    @Autowired
    private IApiMiniAccountCollectService miniAccountCollectService;


    /**
     * 用户添加收藏 or 购物车商品移入收藏
     *
     * @param userCollectDtos
     * @return
     */
    @PostMapping("/plus")
    @ApiOperation(value = "添加用戶收藏")
    public Result addAccountCollect(@RequestBody List<UserCollectDto> userCollectDtos) {
        miniAccountCollectService.addAccountCollect(userCollectDtos);
        return Result.ok();
    }


    /**
     * 获取用户收藏信息
     *
     * @return 用户收藏信息
     */
    @GetMapping("/getUserCollectInfo")
    @ApiOperation(value = "获取用户收藏信息")
    public Result<List<UserCollectVo>> getUserCollectInfo() {
        List<UserCollectVo> userCollectInfo = miniAccountCollectService.getUserCollectInfo();
        return Result.ok(userCollectInfo);
    }


    /**
     * 删除用户收藏信息
     *
     * @param userCollectDto 用户收藏数据
     * @return
     */
    @PostMapping("/delAccountCollect")
    @ApiOperation(value = "删除用户收藏信息")
    public Result deleteAccountCollect(@ApiParam(value = "删除的用戶收藏信息") @RequestBody UserCollectDto userCollectDto) {
        miniAccountCollectService.delAccountCollect(userCollectDto);
        return Result.ok();
    }


    /**
     * 根据商品id 进行查找 查看用户是否收藏过该商品
     *
     * @param productId 商品id
     * @return 是否进行了收藏
     */
    @GetMapping("/findAccountIsCollect")
    @ApiOperation(value = "查看用户是否进行收藏")
    public Result findAccountIsCollect(@ApiParam(value = "产品id", required = true) @RequestParam("productId") Long productId) {
        Boolean accountIsCollect = miniAccountCollectService.findAccountIsCollect(productId);
        return Result.ok(accountIsCollect);
    }

    /**
     * 获取用户收藏数量
     *
     * @return 收藏数量
     */
    @GetMapping("/getCollect/Count")
    @ApiOperation(value = "用户收藏数量")
    public Result getAccountCollectCount() {
        int collectCount = miniAccountCollectService.getCollectCount();
        return Result.ok(collectCount);
    }

}
