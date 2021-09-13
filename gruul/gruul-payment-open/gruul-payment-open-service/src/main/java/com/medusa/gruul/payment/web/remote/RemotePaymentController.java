package com.medusa.gruul.payment.web.remote;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.payment.api.constant.MagicConstant;
import com.medusa.gruul.payment.api.constant.ReturnCodeConstant;
import com.medusa.gruul.payment.api.enums.FeeTypeEnum;
import com.medusa.gruul.payment.api.model.dto.PayRequestDto;
import com.medusa.gruul.payment.api.model.dto.PayResultDto;
import com.medusa.gruul.payment.api.model.dto.PayStatusDto;
import com.medusa.gruul.payment.router.PayMessageRouter;
import com.medusa.gruul.payment.service.IPaymentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author whh
 */
@RestController(value = "remotePaymentController")
@RequestMapping("/")
@Log
@Api(tags = "远程调用接口")
public class RemotePaymentController {

    @Autowired
    private PayMessageRouter payMessageRouter;

    @Autowired
    private IPaymentService paymentService;


    /**
     * 获取指定订单交易状态
     *
     * @param outTradeNo 业务订单号
     * @param payChannel 支付渠道
     * @param tenantId   商户标识
     * @return
     */
    @ApiOperation(value = "获取指定订单交易状态")
    @RequestMapping(value = "/pay/status/{outTradeNo}", method = RequestMethod.GET)
    @EscapeLogin
    PayStatusDto getPayStatus(@PathVariable("outTradeNo") String outTradeNo,
                              @PathVariable("transactionId") String transactionId,
                              @RequestParam("payChannel") String payChannel,
                              @RequestParam("tenantId") String tenantId) {
        return paymentService.getPayStatus(outTradeNo, payChannel, tenantId,transactionId);
    }

    @ApiOperation(value = "发起一笔交易请求")
    @PostMapping(value = "/pay/request")
    @EscapeLogin
    public PayResultDto payRequest(@RequestBody PayRequestDto payRequestDto) {
        log.warning("请求参数 : " + JSONObject.toJSONString(payRequestDto));
        PayResultDto resultDto = parameterVerify(payRequestDto);
        System.out.println(resultDto);
        if (resultDto != null) {
            return resultDto;
        }
        return payMessageRouter.route(payRequestDto);
    }


    /**
     * 校验必要参数是否存在
     *
     * @param payRequestDto dto
     */
    private PayResultDto parameterVerify(PayRequestDto payRequestDto) {
        PayResultDto dto = new PayResultDto();
        System.out.println("payRequestDto{}  "+payRequestDto);
        dto.setReturnCode(ReturnCodeConstant.FAIL);
        //校验必要参数是否存在
        if (StrUtil.isEmpty(payRequestDto.getTenantId())) {
            dto.setReturnMsg("商户标识不能为空");
            return dto;
        }
        if (payRequestDto.getPayChannel() == null || payRequestDto.getPayChannel() <= 0) {
            dto.setReturnMsg("支付渠道不能为空");
            return dto;
        }
        if (StrUtil.isEmpty(payRequestDto.getRouteKey()) && StrUtil.isEmpty(payRequestDto.getNotifyUrl())) {
            dto.setReturnMsg("回调url和路由键必选其一");
            return dto;
        }
        if (payRequestDto.getTradeType() == null || payRequestDto.getTradeType() <= 0) {
            dto.setReturnMsg("交易类型不能为空");
            return dto;
        }
        if (payRequestDto.getTotalFee() == null || payRequestDto.getTotalFee().doubleValue() < MagicConstant.MIN_MONEY.doubleValue()) {
            dto.setReturnMsg("交易总金额不能为空");
            return dto;
        }
        if (StrUtil.isNotEmpty(payRequestDto.getTimeoutExpress()) && !payRequestDto.getTimeoutExpress().matches(MagicConstant.TIMEOUT_EXPRESS_REGEX)) {
            dto.setReturnMsg("最晚付款时间取值错误");
            return dto;
        }
        //设置默认参数
        if (StrUtil.isEmpty(payRequestDto.getTimeoutExpress())) {
            payRequestDto.setTimeoutExpress("15m");
        }
        if (StrUtil.isEmpty(payRequestDto.getFeeType())) {
            payRequestDto.setFeeType(FeeTypeEnum.CNY.getName());
        }
        return null;
    }
}
