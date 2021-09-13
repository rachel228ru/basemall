package com.medusa.gruul.shops.controller;

import cn.hutool.core.bean.BeanUtil;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.common.core.util.SystemCode;
import com.medusa.gruul.shops.api.entity.ShopGuidePageSwitch;
import com.medusa.gruul.shops.service.IShopGuidePageSwitchService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 引导页开关 控制器
 * @Author: xiaoq
 * @Date : 2020/10/17 13:10
 */
@RestController
@RequestMapping(value = "/shop/guide/switch")
public class ShopsGuidePageSwitchController {

    @Autowired
    private IShopGuidePageSwitchService shopGuidePageSwitchService;

    @GetMapping("page/get")
    @ApiOperation(value = "店铺引导页开关获取")
    public Result getShopGuidePageSwitch() {
        ShopGuidePageSwitch shopGuidePageSwitch = shopGuidePageSwitchService.getShopGuidePageSwitch();
        if (BeanUtil.isEmpty(shopGuidePageSwitch)) {
            throw new ServiceException("店铺引导页开关不存在 !", SystemCode.DATA_NOT_EXIST.getCode());
        }
        System.out.println(shopGuidePageSwitch);
        return Result.ok(shopGuidePageSwitch.getOpen());
    }

    @GetMapping("page/update")
    @ApiOperation(value = "店铺引导页开启关闭")
    public Result updateShopGuidePageSwitch(@ApiParam(value = "状态值", required = true) @RequestParam(value = "status") Boolean status) {
        shopGuidePageSwitchService.updateShopGuidePageSwitch(status);
        return Result.ok();
    }
}
