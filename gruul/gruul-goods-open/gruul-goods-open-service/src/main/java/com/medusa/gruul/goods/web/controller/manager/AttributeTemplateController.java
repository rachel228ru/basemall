package com.medusa.gruul.goods.web.controller.manager;


import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.goods.api.model.dto.manager.AttributeTemplateDto;
import com.medusa.gruul.goods.api.model.param.manager.AttributeTemplateParam;
import com.medusa.gruul.goods.api.model.vo.manager.AttributeTemplateVo;
import com.medusa.gruul.goods.service.manager.IAttributeTemplateService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 属性模板
 * </p>
 *
 * @author kyl
 * @since 2019-10-06
 */
@RestController
@RequestMapping("/manager/attribute/template")
public class AttributeTemplateController {

    @Autowired
    private IAttributeTemplateService attributeTemplateService;

    /**
     * 属性模板列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "属性模板列表")
    public Result<PageUtils<AttributeTemplateVo>> getAttributeTemplateList(AttributeTemplateParam attributeTemplateParam) {
        PageUtils<AttributeTemplateVo> pageUtils = new PageUtils<>(attributeTemplateService.getAttributeTemplateList(attributeTemplateParam));
        return Result.ok(pageUtils);
    }

    /**
     * 获取所有属性模板
     */
    @GetMapping("/get/all")
    @ApiOperation(value = "所有属性模板")
    public Result<List<AttributeTemplateVo>> getAllAttributeTemplateList() {
        List<AttributeTemplateVo> attributeTemplateVos = attributeTemplateService.getAllAttributeTemplateList();
        return Result.ok(attributeTemplateVos);
    }


    /**
     * 属性模板新增
     */
    @PostMapping("/save")
    @ApiOperation(value = "属性模板保存")
    public Result addAttributeTemplate(@RequestBody @Validated AttributeTemplateDto attributeTemplateDto) {
        attributeTemplateService.addAttributeTemplate(attributeTemplateDto);
        return Result.ok();
    }

    /**
     * 属性模板修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "属性模板修改")
    public Result updateAttributeTemplate(@RequestBody AttributeTemplateDto attributeTemplateDto) {
        attributeTemplateService.updateAttributeTemplate(attributeTemplateDto);
        return Result.ok();
    }

    /**
     * 属性模板删除
     */
    @DeleteMapping("/delete/{ids}")
    @ApiOperation(value = "属性模板删除")
    public Result deleteAttributeTemplateList(@ApiParam(value = "模板ids", required = true) @PathVariable(name = "ids") Long[] ids) {
        attributeTemplateService.deleteAttributeTemplateList(ids);
        return Result.ok();
    }

    /**
     * 属性模板子属性删除
     */
    @DeleteMapping("/delete/child/{id}")
    @ApiOperation(value = "属性模板子属性删除")
    public Result deleteAttributeTemplateChild(@ApiParam(value = "模板子属性id", required = true) @PathVariable(name = "id") Long id) {
        attributeTemplateService.deleteAttributeTemplateChild(id);
        return Result.ok();
    }

}
