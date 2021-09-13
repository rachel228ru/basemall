package com.medusa.gruul.platform.web.controller;


import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.platform.model.dto.AddInvoiceDto;
import com.medusa.gruul.platform.model.dto.UpdateInvoiceDto;
import com.medusa.gruul.platform.model.vo.InvoiceListVo;
import com.medusa.gruul.platform.service.ISysShopInvoiceRiseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户发票抬头表 前端控制器
 * </p>
 *
 * @author whh
 * @since 2020-08-01
 */
@RestController
@RequestMapping("/invoice")
@Api(tags = "用户发票相关")
public class SysShopInvoiceRiseController {

    @Autowired
    private ISysShopInvoiceRiseService sysShopInvoiceRiseService;

    @PostMapping
    @ApiOperation(value = "新增用户发票抬头")
    public Result addInvoice(@RequestBody @Validated AddInvoiceDto balanceRechargeDto) {
        sysShopInvoiceRiseService.addInvoice(balanceRechargeDto);
        return Result.ok();
    }

    @PutMapping
    @ApiOperation(value = "更新用户发票抬头")
    public Result updateInvoice(@RequestBody @Validated UpdateInvoiceDto updateInvoiceDto) {
        sysShopInvoiceRiseService.updateInvoice(updateInvoiceDto);
        return Result.ok();
    }

    @GetMapping
    @ApiOperation(value = "查询用户自身用户发票抬头")
    public Result<List<InvoiceListVo>> invoiceList(@ApiParam(value = "抬头类型 0-所有 1-个人或事业单位 2-企业", required = true) @RequestParam(required = false, defaultValue = "0") Integer type) {
        List<InvoiceListVo> listVos = sysShopInvoiceRiseService.invoiceList(type);
        return Result.ok(listVos);
    }
}
