package com.medusa.gruul.afs.controller.mini;

import com.medusa.gruul.afs.service.IAfsOrderItemService;
import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.common.core.util.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.HashMap;

/**
 * @Description:
 * @Author: xiaoq
 * @Date : 2021-07-21 20:09
 */
@RestController
@RequestMapping("/mini/user")
public class MiniAfsOrderItemController {
    @Resource
    IAfsOrderItemService afsOrderItemService;
    @ApiOperation("获取当前订单的售后次数")
    @GetMapping("/apply/goods/number")
    @EscapeLogin
    public Result getOrderGoodsApplyNumber(@RequestParam(value = "orderId") @NotNull Long orderId){
        HashMap<String, Object> stringObjectHashMap = afsOrderItemService.userApply(orderId);
        return Result.ok(stringObjectHashMap);
    }
}
