package com.medusa.gruul.shops.controller;


import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.shops.model.param.ShopsRenovationAssemblyParam;
import com.medusa.gruul.shops.model.param.ShopsRenovationPageParam;
import com.medusa.gruul.shops.model.param.ShopsRenovationPluginParam;
import com.medusa.gruul.shops.model.param.ShopsRenovationTemplateParam;
import com.medusa.gruul.shops.model.vo.ShopsRenovationPageVo;
import com.medusa.gruul.shops.service.ShopsRenovationPluginService;
import com.medusa.gruul.shops.service.ShopsRenovationTemPageAssService;
import com.medusa.gruul.shops.service.ShopsRenovationTemPageService;
import com.medusa.gruul.shops.service.ShopsRenovationTemService;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author create by zq
 * @date created in 2020/01/14
 */
@RestController
@RequestMapping(value = "/renovation")
public class ShopsRenovationController {


    @Autowired
    private ShopsRenovationTemService shopsRenovationTemService;

    @Autowired
    private ShopsRenovationTemPageService shopsRenovationTemPageService;

    @Autowired
    private ShopsRenovationPluginService shopsRenovationPluginService;

    @Autowired
    private ShopsRenovationTemPageAssService shopsRenovationTemPageAssService;


    /**
     * 新增 修改商铺装修模板 id null 新增 否则 修改
     *
     * @param param
     * @return Result
     */
    @PostMapping("/template/add")
    @ApiOperation(value = "新增 修改商铺装修模板 id null 新增 否则 修改")
    public Result addTemplate(@RequestBody ShopsRenovationTemplateParam param) {
        return shopsRenovationTemService.addTemplate(param);
    }


    /**
     * 删除商铺装修模板 by ids
     *
     * @param ids
     * @return Result
     */
    @GetMapping("/template/delete")
    @ApiOperation(value = "删除商铺装修模板 by ids")
    public Result delTemplate(@RequestParam @NotNull String ids) {
        return shopsRenovationTemService.delTemplate(ids);
    }


    /**
     * 获取商铺装修模板list
     *
     * @param param
     * @return Result
     */
    @PostMapping("/template/list")
    @ApiOperation(value = "获取商铺装修模板list")
    public Result listTemplate(@RequestBody ShopsRenovationTemplateParam param) {
        return shopsRenovationTemService.listTemplate(param);
    }


    /**
     * 保存模板页面
     *
     * @param param
     * @return Result
     */
    @PutMapping("/template/page/save")
    @ApiOperation(value = "保存模板页面")
    public Result saveTemplatePage(@RequestBody ShopsRenovationPageParam param) {
        return shopsRenovationTemPageService.saveTemplatePage(param);

    }


    /**
     * getByModelId
     *
     * @param modelId
     * @return Result
     */
    @GetMapping("/template/page/modelId")
    @ApiOperation(value = "getByModelId")
    public Result getByModelId(@RequestParam String modelId) {
        return shopsRenovationTemPageService.getByModelId(modelId);
    }


    /**
     * 删除商铺装修模板页面 by ids or 模板ids
     *
     * @param ids
     * @param templateId
     * @return Result
     */
    @GetMapping("/template/page/delete")
    @ApiOperation(value = "删除商铺装修模板 by ids or 模板ids")
    public Result delTemplatePage(@RequestParam(required = false) String ids, @RequestParam(required = false) String templateId) {
        return shopsRenovationTemPageService.delTemplatePage(ids, templateId);
    }


    /**
     * 获取商铺装修模板页面list
     *
     * @param param
     * @return Result
     */
    @GetMapping("/template/page/list")
    @ApiOperation(value = "获取商铺装修模板list")
    public Result listTemplatePage(ShopsRenovationPageParam param) {
        return shopsRenovationTemPageService.listTemplatePage(param);
    }

    /**
     * 获取已经完成装修的商品组件
     * @param param
     * @return
     */
    @ApiOperation(value = "获取商品专区已完成的装修组件")
    @GetMapping("/fitment/end/page/list")
    public  Result fitmentPrefectureEnd(ShopsRenovationPageParam param) {
        return shopsRenovationTemPageService.fitmentPrefectureEndPage(param);
    }


    /**
     * 店铺装修模板 - 全局控件属性配置新增&修改
     *
     * @param param
     * @return Result
     */
    @PostMapping("/plugin/add")
    @ApiOperation(value = "店铺装修模板 - 全局控件属性配置新增&修改")
    public Result addPlugin(@RequestBody ShopsRenovationPluginParam param) {
        return shopsRenovationPluginService.addPlugin(param);
    }


    /**
     * 店铺装修模板 - 批量全局控件属性配置新增&修改
     *
     * @param param
     * @return Result
     */
    @PostMapping("/plugins/add")
    @ApiOperation(value = "店铺装修模板 - 批量全局控件属性配置新增&修改")
    public Result addPlugins(@RequestBody List<ShopsRenovationPluginParam> param) {
        return shopsRenovationPluginService.addPlugins(param);
    }


    /**
     * 店铺装修模板 - 全局控件属性配置逻辑删除 by ids
     *
     * @param ids
     * @return Result
     */
    @GetMapping("/plugin/delete")
    @ApiOperation(value = "删除商铺装修模板 by ids")
    public Result delPlugin(@RequestParam @NotNull String ids) {
        return shopsRenovationPluginService.delPlugin(ids);
    }


    /**
     * 店铺装修模板 - 全局控件属性配置查询list
     *
     * @param param
     * @return Result
     */
    @PostMapping("/plugin/list")
    @ApiOperation(value = "店铺装修模板 - 全局控件属性配置查询list")
    public Result listPlugin(@RequestBody ShopsRenovationPluginParam param) {
        return shopsRenovationPluginService.listPlugin(param);
    }


    /**
     * 店铺装修模板 - 页面插件属性配置新增&修改
     *
     * @param params
     * @return Result
     */
    @PostMapping("/template/page/assembly/add")
    @ApiOperation(value = "店铺装修模板 - 页面插件属性配置新增&修改")
    public Result addTemplatePageAssembly(@RequestBody List<ShopsRenovationAssemblyParam> params) {
        return shopsRenovationTemPageAssService.addTemplatePageAssembly(params);
    }


    /**
     * 店铺装修模板 - 页面插件属性配置逻辑删除 by ids
     *
     * @param ids
     * @return Result
     */
    @GetMapping("/template/page/assembly/delete")
    @ApiOperation(value = "店铺装修模板 - 页面插件属性配置逻辑删除 by ids")
    public Result delTemplatePageAssembly(@RequestParam @NotNull String ids) {
        return shopsRenovationTemPageAssService.delTemplatePageAssembly(ids);
    }


    /**
     * 店铺装修模板 - 页面插件属性配置查询list
     *
     * @param param
     * @return Result
     */
    @PostMapping("/template/page/assembly/list")
    @ApiOperation(value = "店铺装修模板 - 页面插件属性配置查询list")
    public Result listTemplatePageAssembly(@RequestBody ShopsRenovationAssemblyParam param) {
        return shopsRenovationTemPageAssService.listTemplatePageAssembly(param);
    }






    /**
     * 删除默认装修模板 by ids
     *
     * @param ids
     * @return Result
     */
    @GetMapping("/template/def/delete")
    @ApiOperation(value = "删除商铺装修模板 by ids")
    public Result delDefTemplate(@RequestParam @NotNull String ids) {
        return shopsRenovationTemService.delDefTemplate(ids);
    }


    /**
     * 获取默认装修模板list
     *
     * @param param
     * @return Result
     */
    @PostMapping("/template/def/list")
    @ApiOperation(value = "获取商铺装修模板list")
    public Result listDefTemplate(@RequestBody ShopsRenovationTemplateParam param) {
        return shopsRenovationTemService.listDefTemplate(param);
    }


    /**
     * 复制模板 by id
     *
     * @param id
     * @return Result
     */
    @GetMapping("/template/copy")
    @ApiOperation(value = "获取商铺装修模板list")
    public Result copyTemplateById(@RequestParam @NonNull Long id) {
        return shopsRenovationTemService.copyTemplateById(id);
    }


    /**
     * 小程序装修默认聚合接口
     *
     * @return Result
     */
    @GetMapping("/template/def/one")
    @ApiOperation(value = "小程序装修默认聚合接口")
    public Result templateDefOne() {
        return shopsRenovationTemService.templateDefOne();
    }



}
