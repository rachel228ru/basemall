package com.medusa.gruul.platform.web.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.platform.api.model.dto.ShopPackageFunctionDto;
import com.medusa.gruul.platform.api.model.vo.PayInfoVo;
import com.medusa.gruul.platform.model.dto.ConsoleUpdateDto;
import com.medusa.gruul.platform.model.dto.PayInfoUpdataDto;
import com.medusa.gruul.platform.model.dto.ShopInfoConsoleCreateDto;
import com.medusa.gruul.platform.model.vo.LoginShopInfoVo;
import com.medusa.gruul.platform.model.vo.ShopInfoVo;
import com.medusa.gruul.platform.model.vo.ShopViewListVo;
import com.medusa.gruul.platform.model.vo.SysShopPackageVo;
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
@Api(tags = "商户后台店铺相关接口")
public class PlatformShopInfoConsoleController {

    @Autowired
    private IPlatformShopInfoService platformShopInfoService;


    @PostMapping(value = "/console/create")
    @ApiOperation(value = "控制台创建店铺,返回平台店铺id")
    public Result amdinList(@RequestBody @Validated ShopInfoConsoleCreateDto dto) {
        Long platformShopId = platformShopInfoService.consoleCreate(dto);
        return Result.ok(platformShopId);
    }

    @GetMapping(value = "/console/list")
    @ApiOperation(value = "控制台拥有的店铺")
    public Result<PageUtils<List<ShopViewListVo>>> consoleList(@ApiParam(value = "指定页数", required = true) @RequestParam Integer page,
                                                               @ApiParam(value = "数据条数", required = true) @RequestParam Integer size,
                                                               @ApiParam(value = "排序方式 1-升级 2-降序 默认降序", required = true) @RequestParam(defaultValue = "2") Integer orderBy

    ) {
        PageUtils<List<ShopViewListVo>> pageUtils = platformShopInfoService.consoleList(new Page(page, size).addOrder(orderBy.equals(2)? OrderItem.desc("create_time"): OrderItem.asc("create_time")));
        return Result.ok(pageUtils);
    }


    @DeleteMapping(value = "/console/del/{shopId}")
    @ApiOperation(value = "控制台删除")
    public Result consoleDel(@ApiParam(value = "店铺id", required = true) @PathVariable Long shopId) {
        platformShopInfoService.consoleDel(shopId);
        return Result.ok();
    }

    @PutMapping(value = "/console/update/{shopId}")
    @ApiOperation(value = "店铺设置(修改店铺信息)")
    public Result consoleUpdate(@RequestBody @Validated ConsoleUpdateDto dto, @ApiParam(value = "店铺id", required = true) @PathVariable Long shopId) {
        platformShopInfoService.consoleUpdate(dto, shopId);
        return Result.ok();
    }

    @PutMapping(value = "/console/close-open/{shopId}")
    @ApiOperation(value = "控制台营业或打烊,取反只需要传入店铺id,只有营业中或已打烊调用才有效果")
    public Result closeOrOpen(@ApiParam(value = "店铺id", required = true) @PathVariable Long shopId) {
        platformShopInfoService.closeOrOpen(shopId);
        return Result.ok();
    }

    @GetMapping("/console/join/shop/{shopId}")
    @ApiOperation(value = "控制台进入指定店铺后台")
    public Result<LoginShopInfoVo> joinShop(@ApiParam(value = "店铺id", required = true) @PathVariable Long shopId) {
        LoginShopInfoVo infoVo = platformShopInfoService.joinShop(shopId);
        return Result.ok(infoVo);
    }

    @GetMapping("/info")
    @EscapeLogin
    @ApiOperation(value = "根据租户id获取店铺基本信息")
    public Result<ShopInfoVo> shopInfo() {
        ShopInfoVo infoVo = platformShopInfoService.shopInfo();
        return Result.ok(infoVo);
    }


    @GetMapping("/pay/info")
    @EscapeLogin
    @ApiOperation(value = "获取支付配置")
    public Result<PayInfoVo> payInfo(@ApiParam(value = "获取类型 1-默认加密  2-明文数据需带上code", required = true) @RequestParam(value = "type", defaultValue = "1") Integer type,
                                     @ApiParam(value = "扫码校验返回的code") @RequestParam(name = "code", required = false) String code) {
        if (type < CommonConstants.NUMBER_ONE || type > CommonConstants.NUMBER_TWO) {
            throw new ServiceException("获取类型错误");
        }
        if (CommonConstants.NUMBER_TWO.equals(type)) {
            if (StrUtil.isEmpty(code)) {
                throw new ServiceException("无效获取");
            }
        }
        String tenantId = TenantContextHolder.getTenantId();
        PayInfoVo infoVo = platformShopInfoService.payInfo(type, code, tenantId);
        return Result.ok(infoVo);
    }

    @PostMapping("/upload/certificate")
    @EscapeLogin
    @ApiOperation(value = "上传支付证书")
    public Result uploadCertificate(@RequestParam("file") MultipartFile file) {
        String certificatePath = platformShopInfoService.uploadCertificate(file, null);
        return Result.ok(certificatePath);
    }

    @PostMapping("/pay/info")
    @EscapeLogin
    @ApiOperation(value = "商家后台支付配置修改")
    public Result payInfoUpdata(@RequestBody PayInfoUpdataDto payInfoUpdataDto) {
        payInfoUpdataDto.setTenantId(TenantContextHolder.getTenantId());
        platformShopInfoService.payInfoUpdata(payInfoUpdataDto);
        return Result.ok();
    }

    @PostMapping("/default/repair")
    @ApiOperation(value = "重新生成店铺所有默认值")
    public Result defaultRepair() {
        platformShopInfoService.defaultRepair(TenantContextHolder.getTenantId());
        return Result.ok();
    }


    @GetMapping("/package/info")
    @EscapeLogin
    @ApiOperation(value = "获取店铺当前可购买套餐")
    public Result<List<SysShopPackageVo>> getCurrentPackage() {
        List<SysShopPackageVo> infoVo = platformShopInfoService.getCurrentPackage();
//        platformShopInfoService.validateShopPastDue();
        return Result.ok(infoVo);
    }

    /**
     * 根据租户id获取店铺当前使用的套餐功能状态
     * <p>
     * code == 200 返回正确数据
     *
     * @return com.medusa.gruul.platform.api.model.dto.ShopInfoDto
     */
    @GetMapping(value = "/get/shop/function")
    @EscapeLogin
    @ApiOperation(value = "根据租户id获取店铺当前使用的套餐功能状态,当前仅限拼团模板数据")
    public Result<ShopPackageFunctionDto> getShopFunction(@RequestParam(value = "tenantId", required = true) String tenantId) {
        tenantId = TenantContextHolder.getTenantId();
        return platformShopInfoService.getShopFunction(tenantId);
    }
}
