package com.medusa.gruul.goods.web.controller.api;

import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.goods.api.model.dto.api.ApiSupplierDto;
import com.medusa.gruul.goods.service.api.IApiSupplierService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 小程序供应商 前端控制器
 *
 * @author: KYL
 * @since: 2019/11/13
 */
@RestController
@RequestMapping("/api/supplier")
public class ApiSupplierController {

    @Autowired
    private IApiSupplierService supplierService;

    /**
     * 供应商新增
     */
    @PostMapping("/save")
    @ApiOperation(value = "供应商申请")
    public Result addSupplier(@RequestBody @Validated ApiSupplierDto supplierDto) {
        supplierService.addSupplier(supplierDto);
        return Result.ok();
    }

}
