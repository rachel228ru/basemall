package com.medusa.gruul.afs.controller.mini;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.medusa.gruul.afs.api.entity.AfsOrder;
import com.medusa.gruul.afs.api.entity.AfsReason;
import com.medusa.gruul.afs.model.*;
import com.medusa.gruul.afs.service.IAfsOrderService;
import com.medusa.gruul.afs.service.IAfsReasonService;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.common.core.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 售后工单 前端控制器
 * </p>
 *
 * @author alan
 * @since 2020 -08-05
 */
@Api(tags = "小程序端接口")
@RestController
@RequestMapping("/mini/user")
public class MiniAfsOrderController {
    @Resource
    private IAfsOrderService afsOrderService;
    @Resource
    private IAfsReasonService afsReasonsService;

    /**
     * Search order result.
     *
     * @param dto the dto
     * @return the result
     */
    @ApiOperation(value = "用户查询订单", notes = "查询售后订单")
    @GetMapping("/search")
    public Result<PageUtils<ApiAfsOrderVo>> searchOrder(SearchOrderDto dto) {
        PageUtils<ApiAfsOrderVo> page = afsOrderService.searchOrder(dto);
        return Result.ok(page);
    }


    /**
     * Reasons result.
     *
     * @return the result
     */
    @ApiOperation("申请售后的原因")
    @GetMapping("/reasons")
    public Result reasons() {
        List<String> reasonsList =
                afsReasonsService.list(new LambdaQueryWrapper<AfsReason>()
                        .select(AfsReason::getName)).parallelStream()
                        .map(AfsReason::getName)
                        .collect(Collectors.toList());
        return Result.ok(reasonsList);
    }

    /**
     * User apply result.
     *
     * @param dto the dto
     * @return the result
     */
    @ApiOperation("用户申请售后")
    @PostMapping("/apply")
    public Result userApply(@RequestBody @Validated UserApplyDto dto) {
        AfsOrder afsOrder = afsOrderService.userApply(dto);
        return Result.ok(afsOrder.getId());
    }

    /**
     * Gets order apply number.
     *
     * @param orderId the order id
     * @return the order apply number
     */
    @ApiOperation("获取当前订单的售后次数")
    @GetMapping("/apply/number")
    public Result getOrderApplyNumber(@RequestParam(value = "orderId") @NotNull Long orderId) {
        Integer number = afsOrderService.getUserApplyNumber(orderId);
        Boolean expire = afsOrderService.getAfsExpire(orderId);
        HashMap<String, Object> res = new HashMap<>(2);
        res.put("number", number);
        res.put("expire", expire);
        return Result.ok(res);
    }

    /**
     * Gets return address.
     *
     * @param orderId the order id
     * @return the return address
     */
    @ApiOperation("获取当前售后的退货地址")
    @GetMapping("/return/address")
    public Result getReturnAddress(@RequestParam(value = "orderId") @NotNull Long orderId) {
        ReturnAddressVo returnAddressVo = afsOrderService.getReturnAddress(orderId);
        return Result.ok(returnAddressVo);
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

    /**
     * User cancel result.
     *
     * @param afsId the afs id
     * @return the result
     */
    @ApiOperation("用户取消申请")
    @PutMapping("/cancel")
    public Result userCancel(@RequestParam(value = "afsId") @NotNull Long afsId) {
        afsOrderService.userCancel(afsId, false);
        return Result.ok();
    }

    /**
     * User return result.
     *
     * @param afsId           the afs id
     * @param deliveryCode    the delivery code
     * @param deliveryCompany the delivery company
     * @param deliverySn      the delivery sn
     * @param phone           the phone
     * @param reason          the reason
     * @return the result
     */
    @ApiOperation("用户确认已退货")
    @PutMapping("/return")
    public Result userReturn(@RequestParam(value = "afsId") @NotNull Long afsId,
                             @RequestParam(value = "deliveryCode", required = false) String deliveryCode,
                             @RequestParam(value = "deliveryCompany", required = false) String deliveryCompany,
                             @RequestParam(value = "deliverySn", required = false) String deliverySn,
                             @RequestParam(value = "phone", required = false) String phone,
                             @RequestParam(value = "reason", required = false) String reason
    ) {
        afsOrderService.userReturn(afsId, deliveryCode, deliveryCompany, deliverySn, phone, reason);
        return Result.ok();
    }

}
