package com.medusa.gruul.afs.controller.remote;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.medusa.gruul.afs.api.entity.AfsOrder;
import com.medusa.gruul.afs.api.model.AfsSimpleVo;
import com.medusa.gruul.afs.service.IAfsOrderService;
import com.medusa.gruul.common.core.annotation.EscapeLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * The type Remote afs controller.
 *
 * @author alan
 * @description: RemoteAfsController.java
 * @date 2020 /10/5 09:59
 */
@RestController
@RequestMapping("/remote")
@Api(tags = "Feign接口")
public class RemoteAfsController {
    @Resource
    private IAfsOrderService afsOrderService;

    /**
     * 售后单详情,该方法使用Feign默认租户信息，不包含默认查询全部
     *
     * @param receiptBillId the receipt bill id
     * @return the afs order by receipt bill id
     */
    @EscapeLogin
    @ApiOperation("售后单详情")
    @GetMapping("/{receiptBillId}")
    public List<AfsSimpleVo> getAfsOrderByReceiptBillId(@PathVariable(value = "receiptBillId") @NotNull Long receiptBillId) {
        return afsOrderService.getAfsOrderByReceiptBillId(receiptBillId);
    }



    /**
     * 获取用户订单数量
     * @param userId
     * @return
     */
    @GetMapping("/remote/afsOrderNumber")
    Long getAfsOrderNumber(@RequestParam(value = "userId") String userId) {
        List<AfsOrder> afsOrders = afsOrderService.getBaseMapper().selectList(new LambdaQueryWrapper<AfsOrder>()
                .eq(AfsOrder::getUserId, userId));
        long size = afsOrders.size();
        return size;
    }

}
