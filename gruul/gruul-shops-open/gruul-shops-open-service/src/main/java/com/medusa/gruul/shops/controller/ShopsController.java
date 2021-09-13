package com.medusa.gruul.shops.controller;


import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.shops.model.param.ShopsSearchTermsParam;
import com.medusa.gruul.shops.model.vo.ShopsSearchTermsVo;
import com.medusa.gruul.shops.service.ShopsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author create by zq
 * @date created in 2020/01/14
 */
@RestController
@RequestMapping(value = "/shop")
public class ShopsController {


    @Autowired
    private ShopsService shopsService;


    /**
     * 获取商铺
     *
     * @return list
     */
    @PostMapping("/list")
    @ApiOperation(value = "获取商铺")
    public List listShops() {
        return shopsService.listShops();
    }


    /**
     * 获取商铺热搜词汇信息
     *
     * @return Result
     */
    @GetMapping("/terms")
    @ApiOperation(value = "获取商铺热搜词汇信息")
    public Result getTerms() {
        return shopsService.getTerms();
    }


    /**
     * 新增保存商铺热搜词汇信息
     *
     * @param param
     * @return Result
     */
    @PutMapping("/terms/update")
    @ApiOperation(value = "新增保存商铺热搜词汇信息")
    public Result updateTerms(@RequestBody ShopsSearchTermsParam param) {
        return shopsService.updateTerms(param);
    }

}
