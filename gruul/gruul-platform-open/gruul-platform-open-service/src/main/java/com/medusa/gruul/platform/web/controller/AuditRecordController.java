package com.medusa.gruul.platform.web.controller;

import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.platform.model.vo.AuditRecordVo;
import com.medusa.gruul.platform.service.IAuditRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 小程序审核记录表 前端控制器
 * </p>
 *
 * @author whh
 * @since 2019-09-07
 */
@RestController
@RequestMapping("/audit-record")
@Api(tags = "小程序相关接口")
public class AuditRecordController {

    @Autowired
    private IAuditRecordService auditRecordService;

    @GetMapping("audit-record")
    @ApiOperation(value = "小程序审核记录")
    public Result<PageUtils<AuditRecordVo>> getMiniAuditRecordList(@ApiParam(value = "指定页数", required = true) @RequestParam Integer page,
                                                                   @ApiParam(value = "数据条数", required = true) @RequestParam Integer size,
                                                                   @ApiParam(value = "小程序appId") @RequestParam String appId) {
        PageUtils<AuditRecordVo> list = auditRecordService.getMiniAuditRecordList(page, size, appId);
        return Result.ok(list);
    }

}
