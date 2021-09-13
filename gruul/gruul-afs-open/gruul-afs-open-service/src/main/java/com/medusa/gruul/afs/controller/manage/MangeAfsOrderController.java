package com.medusa.gruul.afs.controller.manage;


import com.medusa.gruul.afs.model.*;
import com.medusa.gruul.afs.service.IAfsOrderService;
import com.medusa.gruul.afs.service.IManageAfsOrderService;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.common.core.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 售后工单 前端控制器
 * </p>
 *
 * @author alan
 * @since 2020 -08-05
 */
@RestController
@Api(tags = "PC端接口")
@RequestMapping("/manage")
public class MangeAfsOrderController {
    @Resource
    private IManageAfsOrderService manageAfsOrderService;
    @Resource
    private IAfsOrderService afsOrderService;

    /**
     * List result.
     *
     * @param dto the dto
     * @return the result
     */
    @ApiOperation(value = "商家售后列表查看")
    @GetMapping("/list")
    public Result<PageUtils<ManageAfsOrderVo>> list(SearchDto dto) {
        PageUtils<ManageAfsOrderVo> page = manageAfsOrderService.searchManageAfsOrderVoPage(dto);
        return Result.ok(page);
    }


    /**
     * Seller refusal result.
     *
     * @param dto the dto
     * @return the result
     */
    @ApiOperation(value = "商家拒绝申请")
    @PostMapping("/seller/refusal")
    public Result sellerRefusal(@RequestBody @Validated SellerRefusalDto dto) {
        manageAfsOrderService.sellerRefuse(dto);
        return Result.ok();
    }

    /**
     * Seller approve result.
     *
     * @param afsId the afs id
     * @return the result
     */
    @ApiOperation(value = "商家同意申请")
    @PutMapping("/seller/approve")
    public Result sellerApprove(@RequestParam(value = "afsId") @NotNull Long afsId) {
        manageAfsOrderService.sellerApprove(afsId, false);
        return Result.ok();
    }

    /**
     * Note result.
     *
     * @param dto the dto
     * @return the result
     */
    @ApiOperation(value = "商家批量备注订单", notes = "商家批量备注订单")
    @PostMapping("/note")
    public Result note(@RequestBody @Validated SellerNoteDto dto) {
        manageAfsOrderService.note(dto);
        return Result.ok();
    }

    /**
     * Gets afs order info.
     *
     * @param afsId the afs id
     * @return the afs order info
     */
    @ApiOperation("根据售后单ID获取售后详情")
    @GetMapping("/info")
    public Result getAfsOrderInfo(@RequestParam(value = "afsId") @NotNull Long afsId) {
        AfsOrderVo vo = afsOrderService.getAfsOrderInfo(afsId);
        return Result.ok(vo);
    }

}
