package com.medusa.gruul.goods.web.controller.manager;


import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.goods.api.model.dto.manager.SaleModeDto;
import com.medusa.gruul.goods.api.model.param.manager.SaleModeParam;
import com.medusa.gruul.goods.api.model.vo.manager.SaleModeVo;
import com.medusa.gruul.goods.service.manager.ISaleModeService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 商品自定义专区 前端控制器
 * </p>
 *
 * @author alan
 * @since 2019-09-03
 */
@RestController
@RequestMapping("/manager/sale/mode")
public class SaleModeController {

    @Autowired
    private ISaleModeService saleModeService;

    /**
     * 获取商品专区列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "商品专区列表")
    public Result<PageUtils<SaleModeVo>> getSaleModeList(SaleModeParam saleModeParam) {
        PageUtils<SaleModeVo> pageUtils = new PageUtils<>(saleModeService.getSaleModeList(saleModeParam));
        return Result.ok(pageUtils);
    }

    /**
     * 获取所有商品专区列表
     */
    @GetMapping("/get/all")
    @ApiOperation(value = "商品所有专区列表")
    public Result<List<SaleModeVo>> getAllSaleModeList() {
        List<SaleModeVo> saleModeVos = saleModeService.getAllSaleModeList();
        return Result.ok(saleModeVos);
    }

    /**
     * 商品专区新增
     */
    @PostMapping("/save")
    @ApiOperation(value = "商品专区新增")
    public Result addSaleMode(@RequestBody @Validated List<SaleModeDto> saleModeDtos) {
        saleModeService.addSaleMode(saleModeDtos);
        return Result.ok();
    }

    /**
     * 商品专区修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "商品专区修改")
    public Result updateSaleMode(@RequestBody @Validated SaleModeDto saleModeDto) {
        saleModeService.updateSaleMode(saleModeDto);
        return Result.ok();
    }

    /**
     * 商品专区删除
     */
    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "商品专区删除")
    public Result deleteShowCategoryList(@ApiParam(value = "展示分类id", required = true) @PathVariable(name = "id") Long id) {
        saleModeService.deleteSaleMode(id);
        return Result.ok();
    }

    /**
     * 商品专区排序
     */
    @PutMapping("/sort")
    @ApiOperation(value = "商品专区排序")
    public Result updateSaleModeSort(@RequestBody List<SaleModeDto> saleModeDtos) {
        saleModeService.updateSaleModeSort(saleModeDtos);
        return Result.ok();
    }

//    /**
//     * 展示、分拣、商品专区老数据专区更新
//     */
//    @PutMapping("/refresh/data")
//    @ApiOperation(value = "展示、分拣、商品专区老数据专区更新")
//    @EscapeLogin
//    public Result refreshSaleModeData() {
//        saleModeService.refreshSaleModeData();
//        return Result.ok();
//    }

}
