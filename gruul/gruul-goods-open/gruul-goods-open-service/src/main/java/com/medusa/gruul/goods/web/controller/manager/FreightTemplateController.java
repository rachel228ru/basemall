package com.medusa.gruul.goods.web.controller.manager;


import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.goods.service.manager.IFreightTemplateService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 运费模版 前端控制器
 * </p>
 *
 * @author lcysike
 * @since 2019-10-01
 */
@RestController
@RequestMapping("/manager/freight/template")
public class FreightTemplateController {

    @Autowired
    private IFreightTemplateService freightTemplateService;

    /**
     * 物流接口获取所有运费模版
     */
    @GetMapping("/list/by/logistics")
    @ApiOperation(value = "物流接口获取所有运费模版")
    public Result findFreightTemplateListByLogistics() {
        return freightTemplateService.findFreightTemplateListByLogistics();
    }

    /**
     * 物流接口获取单个产品运费模版信息
     */
    @GetMapping("/info/by/logistics/{freightTemplateId}")
    @ApiOperation(value = "物流接口获取单个产品运费模版信息")
    public Result findFreightTemplateInfoByLogistics(@ApiParam(value = "运费模版id", required = true) @PathVariable("freightTemplateId") Long freightTemplateId) {
        return freightTemplateService.findFreightTemplateInfoByLogistics(freightTemplateId);
    }

}
