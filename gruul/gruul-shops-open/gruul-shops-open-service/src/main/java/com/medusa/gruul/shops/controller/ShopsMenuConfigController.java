package com.medusa.gruul.shops.controller;


import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.shops.model.param.ShopsMenuConfigParam;
import com.medusa.gruul.shops.service.ShopsMenuConfigService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author create by zq
 * @date created in 2020/01/14
 */
@RestController
@RequestMapping(value = "/shop/menu")
public class ShopsMenuConfigController {


    @Autowired
    private ShopsMenuConfigService shopsMenuConfigService;


    /**
     * 获取店铺合伙人菜单属性配置
     *
     * @return Result
     */
    @GetMapping("/one")
    @ApiOperation(value = "获取店铺合伙人菜单属性配置")
    public Result getMenuConfig() {
        return shopsMenuConfigService.getMenuConfig();
    }


    /**
     * id为空 新增 / id 非空 修改 [店铺合伙人菜单属性配置]
     *
     * @param param
     * @return Result
     */
    @PutMapping("/update")
    @ApiOperation(value = "id为空 新增 / id 非空 修改 [店铺合伙人菜单属性配置] ")
    public Result updateTerms(@RequestBody ShopsMenuConfigParam param) {
        return shopsMenuConfigService.updateMenuConfig(param);
    }

}
