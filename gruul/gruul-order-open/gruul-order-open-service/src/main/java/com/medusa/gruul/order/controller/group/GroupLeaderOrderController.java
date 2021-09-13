package com.medusa.gruul.order.controller.group;


import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.order.model.*;
import com.medusa.gruul.order.service.IGroupLeaderOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 *
 * @author alan
 * @date 2019/11/13 20:14
 */
@RestController
@RequestMapping("/group")
@Api(tags = "订单接口")
public class GroupLeaderOrderController {
    @Resource
    private IGroupLeaderOrderService groupLeaderOrderService;


    @ApiOperation("用户概况列表")
    @GetMapping("/user/overview")
    public Result<List<UserOverviewVo>> getUserOverviewPage(@RequestParam(value = "keyword", required = false) String keyword) {
        List<UserOverviewVo> list = groupLeaderOrderService.getUserOverviewPage(keyword);
        return Result.ok(list);
    }

    @ApiOperation("用户订单列表")
    @GetMapping("/user/order")
    public Result<PageUtils<SimpleOrderVo>> getOrderPage(QueryUserOrderDto dto) {
        PageUtils<SimpleOrderVo> page = groupLeaderOrderService.getOrderPage(dto);
        return Result.ok(page);
    }

}
