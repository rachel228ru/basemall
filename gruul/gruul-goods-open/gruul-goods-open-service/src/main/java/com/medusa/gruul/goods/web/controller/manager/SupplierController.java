package com.medusa.gruul.goods.web.controller.manager;

import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.goods.api.model.dto.manager.SupplierDto;
import com.medusa.gruul.goods.api.model.param.manager.ProductParam;
import com.medusa.gruul.goods.api.model.param.manager.SupplierParam;
import com.medusa.gruul.goods.api.model.vo.manager.ProductVo;
import com.medusa.gruul.goods.api.model.vo.manager.SupplierVo;
import com.medusa.gruul.goods.service.manager.IProductService;
import com.medusa.gruul.goods.service.manager.ISupplierService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 供应商 前端控制器
 *
 * @author kyl
 * @since 2019-10-06
 */
@RestController
@RequestMapping("/manager/supplier")
public class SupplierController {
    @Autowired
    private ISupplierService supplierService;

    @Autowired
    private IProductService productService;

    /**
     * 供应商商品列表
     */
    @GetMapping("/product")
    @ApiOperation(value = "供应商商品列表")
    public Result<PageUtils<ProductVo>> getSupplierProductList(ProductParam productParam) {
        PageUtils<ProductVo> pageUtils = new PageUtils(productService.getSupplierProductList(productParam));
        return Result.ok(pageUtils);
    }

    /**
     * 获取所有供应商
     */
    @GetMapping("/get/all")
    @ApiOperation(value = "所有供应商")
    public Result<List<SupplierVo>> getAllSupplierList() {
        List<SupplierVo> supplierVos = supplierService.getAllSupplierList();
        return Result.ok(supplierVos);
    }

    /**
     * 供应商列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "供应商列表")
    public Result<PageUtils<SupplierVo>> getSupplierList(SupplierParam supplierParam) {
        PageUtils<SupplierVo> pageUtils = new PageUtils<>(supplierService.getSupplierList(supplierParam));
        return Result.ok(pageUtils);
    }


    /**
     * 供应商新增
     */
    @PostMapping("/save")
    @ApiOperation(value = "供应商保存")
    public Result addSupplier(@RequestBody @Validated SupplierDto supplierDto) {
        supplierService.addSupplier(supplierDto);
        return Result.ok();
    }

    /**
     * 供应商修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "供应商修改")
    public Result updateSupplier(@RequestBody SupplierDto supplierDto) {
        supplierService.updateSupplier(supplierDto);
        return Result.ok();
    }

    /**
     * 供应商审核
     */
    @PutMapping("/check")
    @ApiOperation(value = "供应商审核")
    public Result checkSupplier(@RequestBody SupplierDto supplierDto) {
        supplierService.checkSupplier(supplierDto);
        return Result.ok();
    }

    /**
     * 供应商删除
     */
    @DeleteMapping("/delete/{ids}")
    @ApiOperation(value = "供应商删除")
    public Result deleteSupplierList(@ApiParam(value = "供应商ids", required = true) @PathVariable(name = "ids") Long[] ids) {
        supplierService.deleteSupplierList(ids);
        return Result.ok();
    }
}
