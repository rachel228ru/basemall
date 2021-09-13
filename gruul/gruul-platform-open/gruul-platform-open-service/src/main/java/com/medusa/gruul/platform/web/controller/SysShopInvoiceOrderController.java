package com.medusa.gruul.platform.web.controller;


import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.platform.model.dto.InvoiceOrderAuditDto;
import com.medusa.gruul.platform.model.dto.ShopInvoiceOrderApplyDto;
import com.medusa.gruul.platform.model.vo.InvoiceOrderApplyVo;
import com.medusa.gruul.platform.service.ISysShopInvoiceOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 发票工单表 前端控制器
 * </p>
 *
 * @author whh
 * @since 2020-08-01
 */
@RestController
@RequestMapping("/sys-shop-invoice-order")
@Api(tags = "用户发票相关")
public class SysShopInvoiceOrderController {

    @Autowired
    private ISysShopInvoiceOrderService sysShopInvoiceOrderService;

    @PostMapping
    @ApiOperation(value = "申请开票接口")
    public Result apply(@RequestBody @Validated ShopInvoiceOrderApplyDto orderApplyDto) {
        sysShopInvoiceOrderService.apply(orderApplyDto);
        return Result.ok();
    }

    @GetMapping
    @EscapeLogin
    @ApiOperation(value = "发票管理查询")
    public Result<PageUtils<InvoiceOrderApplyVo>> applyList(@ApiParam(value = "指定页数", required = true) @RequestParam Integer page,
                                                            @ApiParam(value = "数据条数", required = true) @RequestParam Integer size,
                                                            @ApiParam(value = "工单状态 0待审核，1已审核") @Valid @NotNull @Range(min = 0, max = 1) @RequestParam Integer status,
                                                            @ApiParam(value = "抬头类型：1个人或事业单位，2企业") @RequestParam Integer headType,
                                                            @ApiParam(value = "开始时间") @RequestParam(required = false) String startTime,
                                                            @ApiParam(value = "结束时间") @RequestParam(required = false) String endTime
    ) {
        PageUtils<InvoiceOrderApplyVo> pageUtils = sysShopInvoiceOrderService.applyList(page, size, headType, startTime, endTime, status);
        return Result.ok(pageUtils);
    }


    @PutMapping("/audit")
    @EscapeLogin
    @ApiOperation(value = "开票工单审核接口")
    public Result audit(@RequestBody @Validated InvoiceOrderAuditDto orderAuditDto) {
        sysShopInvoiceOrderService.audit(orderAuditDto);
        return Result.ok();
    }

}
