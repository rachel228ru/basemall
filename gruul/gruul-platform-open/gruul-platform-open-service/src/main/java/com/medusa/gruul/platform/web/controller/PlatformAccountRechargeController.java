package com.medusa.gruul.platform.web.controller;


import com.ijpay.alipay.AliPayApi;
import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.platform.model.dto.BalanceRechargeDto;
import com.medusa.gruul.platform.model.dto.OrderOptionDto;
import com.medusa.gruul.platform.model.vo.BalanceRechargeVo;
import com.medusa.gruul.platform.model.vo.PayRecordVo;
import com.medusa.gruul.platform.model.vo.RechargeConsoleOrderVo;
import com.medusa.gruul.platform.model.vo.RechargeRecordOrderVo;
import com.medusa.gruul.platform.service.IPlatformAccountRechargeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 充值订单表 前端控制器
 * </p>
 *
 * @author whh
 * @since 2020-08-01
 */
@RestController
@RequestMapping("/recharge")
@Api(tags = "商户充值相关接口")
public class PlatformAccountRechargeController {

    @Autowired
    private IPlatformAccountRechargeService platformAccountRechargeService;

    @PostMapping("/balance/pay")
    @ApiOperation(value = "商户余额充值接口")
    public Result<BalanceRechargeVo> balanceRecharge(@RequestBody @Validated BalanceRechargeDto balanceRechargeDto) {
        BalanceRechargeVo vo = platformAccountRechargeService.balanceRecharge(balanceRechargeDto);
        return Result.ok(vo);
    }

    @GetMapping("/{orderId}")
    @ApiOperation(value = "查询指定订单是否支付成功,支付成功返回date=true")
    public Result<Boolean> balanceRecharge(@PathVariable(name = "orderId") Long orderId) {
        Boolean flag = platformAccountRechargeService.orderPayIfOk(orderId);
        return Result.ok(flag);
    }


    @PostMapping("/balance/notify/wx")
    @EscapeLogin
    @ApiOperation(value = "商户余额充值回调接口", hidden = true)
    public String rechargeNotifyWx(HttpServletRequest request) throws IOException {
        String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
        return platformAccountRechargeService.rechargeNotifyWx(xmlResult);
    }


    @PostMapping(value = "/balance/notify/alipay")
    @EscapeLogin
    @ApiOperation(value = "套餐支付微信回调", hidden = true)
    public String notifyAlipay(HttpServletRequest request) {
        return platformAccountRechargeService.rechargeNotifyAlipay(AliPayApi.toMap(request));
    }


    @GetMapping("/order")
    @EscapeLogin
    @ApiOperation(value = "管理台分页查询充值订单")
    public Result<PageUtils<RechargeRecordOrderVo>> payOrder(
            @ApiParam(value = "指定页数", required = true) @RequestParam Integer page,
            @ApiParam(value = "数据条数", required = true) @RequestParam Integer size,
            @ApiParam(value = "支付方式 0-所有 1支付宝 2 微信 3 汇款支付 4-余额支付（代理余额）") @RequestParam(required = false, defaultValue = "0") Integer payType,
            @ApiParam(value = "充值状态 0-所有 1-充值中 2-充值成功 3-订单关闭") @RequestParam(required = false, defaultValue = "0") Integer status,
            @ApiParam(value = "开始时间") @RequestParam(required = false) String payStartTime,
            @ApiParam(value = "结束时间") @RequestParam(required = false) String payEndTime,
            @ApiParam(value = "手机号") @RequestParam(required = false) String phone,
            @ApiParam(value = "充值订单号") @RequestParam(required = false) String rechargeNum,
            @ApiParam(value = "商户昵称") @RequestParam(required = false) String nikeName
    ) {
        PageUtils<RechargeRecordOrderVo> record = platformAccountRechargeService.payOrder(page, size, status, payStartTime, payEndTime, phone, rechargeNum, nikeName, payType);
        return Result.ok(record);
    }

    @PutMapping("/order/option")
    @EscapeLogin
    @ApiOperation(value = "管理台充值订单操作")
    public Result orderOption(@RequestBody OrderOptionDto dto) {
        // TODO 接口阉割 二开可酌情恢复
        return Result.ok();
    }


    @GetMapping("/console/order")
    @ApiOperation(value = "商户查询自己的充值订单")
    public Result<PageUtils<RechargeConsoleOrderVo>> consoleOrders(
            @ApiParam(value = "指定页数", required = true) @RequestParam Integer page,
            @ApiParam(value = "数据条数", required = true) @RequestParam Integer size,
            @ApiParam(value = "充值状态 0-所有 1-充值中 2-充值成功  3-订单关闭") @RequestParam(required = false, defaultValue = "0") Integer status,
            @ApiParam(value = "平台类型  1-系统管理员  2-商家") @RequestParam(required = false) Integer platfromType,
            @ApiParam(value = "开始时间") @RequestParam(required = false) String payStartTime,
            @ApiParam(value = "结束时间") @RequestParam(required = false) String payEndTime
    ) {
        // TODO 接口阉割 二开可酌情恢复
        return Result.ok();
    }


}
