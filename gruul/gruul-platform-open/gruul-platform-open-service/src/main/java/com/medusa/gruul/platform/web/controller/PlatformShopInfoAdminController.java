package com.medusa.gruul.platform.web.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.platform.api.model.vo.PayInfoVo;
import com.medusa.gruul.platform.model.dto.PayInfoUpdataDto;
import com.medusa.gruul.platform.model.dto.ShopInfoAdminCreateDto;
import com.medusa.gruul.platform.model.vo.AccountInfoVo;
import com.medusa.gruul.platform.model.vo.ShopViewListVo;
import com.medusa.gruul.platform.service.IPlatformShopInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 店铺信息表 前端控制器
 * </p>
 *
 * @author whh
 * @since 2020-03-06
 */
@RestController
@RequestMapping("/shop")
@Api(tags = "管理台店铺相关接口")
public class PlatformShopInfoAdminController {

    @Autowired
    private IPlatformShopInfoService platformShopInfoService;

    @PostMapping(value = "/amdin/create")
    @EscapeLogin
    @ApiOperation(value = "管理台创建店铺")
    public Result adminCreate(@RequestBody @Validated ShopInfoAdminCreateDto dto) {
        platformShopInfoService.adminCreate(dto);
        return Result.ok();
    }

    @GetMapping(value = "/amdin/list")
    @EscapeLogin
    @ApiOperation(value = "管理台查询所有店铺")
    public Result<PageUtils<List<ShopViewListVo>>> amdinList(@ApiParam(value = "指定页数", required = true) @RequestParam Integer page,
                                                             @ApiParam(value = "数据条数", required = true) @RequestParam Integer size,
                                                             @ApiParam(value = "模板id", required = false) @RequestParam(required = false) Integer templateInfoId,
                                                             @ApiParam(value = "模板版本id", required = false) @RequestParam(required = false) Integer versionId,
                                                             @ApiParam(value = "模糊搜索,店铺名称商家拥有者手机号", required = false) @RequestParam(required = false) String search,
                                                             @Deprecated @ApiParam(value = "部署方式 是否私有化部署（0官方部署 1独立部署）s1.1.3取消", required = false) @RequestParam(required = false) Integer deployType,
                                                             @ApiParam(value = "店铺状态 1部署中 2正常 ，3已打烊，4禁用", required = false) @RequestParam(required = false) Integer shopStatus,
                                                             @ApiParam(value = "套餐id", required = false) @RequestParam(required = false) Long packageId,
                                                             @ApiParam(value = "代理id", required = false) @RequestParam(required = false) Long agentId
    ) {
        //Todo 业务删除。控制层留用
        return Result.ok();
    }

    @DeleteMapping(value = "/amdin/del/{shopId}")
    @ApiOperation(value = "管理台删除店铺")
    @EscapeLogin
    public Result amdinDel(@PathVariable Long shopId) {
        //Todo 业务删除。控制层留用
        return Result.ok();
    }

    @PutMapping(value = "/admin/close-open/{shopId}")
    @ApiOperation(value = "管理台营业禁用或开启,取反只需要传入店铺id")
    @EscapeLogin
    public Result adminCloseOrOpen(@ApiParam(value = "店铺id", required = true) @PathVariable Long shopId) {
        //Todo 业务删除。控制层留用
        return Result.ok();
    }


    @GetMapping("/admin/join/shop/{shopId}")
    @ApiOperation(value = "管理台进入指定店铺后台")
    @EscapeLogin
    public Result<AccountInfoVo> adminJoinShop(@ApiParam(value = "店铺id", required = true) @PathVariable Long shopId) {
        AccountInfoVo infoVo = platformShopInfoService.adminJoinShop(shopId);
        return Result.ok(infoVo);
    }


    @GetMapping("/admin/pay/mode/{shopId}")
    @EscapeLogin
    @ApiOperation(value = "管理台使用获取指定商户支付配置", tags = "管理台服务商相关")
    public Result<PayInfoVo> payMode(@ApiParam(value = "店铺id", required = true) @PathVariable Long shopId) {
        //Todo 业务删除。控制层留用
        return Result.ok();
    }

    @PostMapping("/admin/pay/info")
    @EscapeLogin
    @ApiOperation(value = "管理台修改指定商户支付配置修改", tags = "管理台服务商相关")
    public Result managerPayInfoUpdata(@RequestBody PayInfoUpdataDto payInfoUpdataDto) {
        //Todo 业务删除。控制层留用
        return Result.ok();
    }

    @PostMapping("/admin/upload/certificate/{shopId}")
    @EscapeLogin
    @ApiOperation(value = "上传支付证书", tags = "管理台服务商相关")
    public Result uploadCertificate(@RequestParam("file") MultipartFile file, @ApiParam(value = "店铺id", required = true) @PathVariable Long shopId) {
        String certificatePath = platformShopInfoService.uploadCertificate(file, shopId);
        return Result.ok(certificatePath);
    }

}
