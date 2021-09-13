package com.medusa.gruul.platform.web.controller;


import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.platform.model.dto.ServiceLibrariesDto;
import com.medusa.gruul.platform.model.vo.BaseLibrariesVo;
import com.medusa.gruul.platform.model.vo.LibrariesServiceListVo;
import com.medusa.gruul.platform.service.IPlatformLibrariesInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 基础库信息表 前端控制器
 * </p>
 *
 * @author alan
 * @since 2020-02-27
 */
@RestController
@RequestMapping("/platform-libraries-info")
@Api(tags = "基础库相关接口")
public class PlatformLibrariesInfoController {


    @Autowired
    private IPlatformLibrariesInfoService platformLibrariesInfoService;


    @ApiOperation(value = "获取基础库数据")
    @GetMapping
    @EscapeLogin
    public Result<PageUtils<BaseLibrariesVo>> getBaseLibraries(@ApiParam(value = "指定页数", required = true) @RequestParam Integer page,
                                                               @ApiParam(value = "数据条数", required = true) @RequestParam Integer size,
                                                               @ApiParam(value = "搜索分类类型：1 业务基础库 2 支撑基础库")
                                                               @RequestParam(required = false) Integer categoryType) {
        PageUtils<BaseLibrariesVo> librariesVos = platformLibrariesInfoService.getBaseLibraries(page, size, categoryType);
        return Result.ok(librariesVos);
    }


    @ApiOperation(value = "查询指定基础库服务")
    @GetMapping(value = "/{libraiesId}")
    @EscapeLogin
    public Result<PageUtils<LibrariesServiceListVo>> getByBaseLibrariesId(@ApiParam(value = "指定页数", required = true) @RequestParam Integer page,
                                                                          @ApiParam(value = "数据条数", required = true) @RequestParam Integer size,
                                                                          @ApiParam(value = "查询服务类型 0-通用服务,1定制服务,当基础库id数据为支撑基础库数据id时,此参数无效",
                                                                                  required = true)
                                                                          @RequestParam(defaultValue = "1") Integer serviceType,
                                                                          @ApiParam(value = "基础库id")
                                                                          @PathVariable(value = "libraiesId") Integer libraiesId) {
        PageUtils<LibrariesServiceListVo> librariesVos = platformLibrariesInfoService.getByBaseLibrariesId(page, size, serviceType, libraiesId);
        return Result.ok(librariesVos);
    }


    @ApiOperation(value = "创建业务基础库")
    @PostMapping(value = "/service/libraries")
    @EscapeLogin
    public Result createServiceLibraries(@Validated @RequestBody ServiceLibrariesDto dto) {
        platformLibrariesInfoService.createServiceLibraries(dto);
        return Result.ok();
    }

    @ApiOperation(value = "更新业务基础库或支撑基础库")
    @PutMapping(value = "/service/libraries")
    @EscapeLogin
    public Result updateServiceLibraries(@Validated @RequestBody ServiceLibrariesDto dto) {
        //Todo 业务删除。控制层留用
        return Result.ok();
    }

    @ApiOperation(value = "删除已下线的业务基础库")
    @PutMapping(value = "/service/libraries/{id}")
    @EscapeLogin
    public Result delete(@ApiParam(value = "业务基础库id") @PathVariable Long id) {
        //Todo 业务删除。控制层留用
        return Result.ok();
    }
}
