package com.medusa.gruul.payment.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.payment.api.entity.PaymentRefund;
import com.medusa.gruul.payment.api.model.dto.EntPayReQuestDto;
import com.medusa.gruul.payment.api.model.dto.RefundRequestDto;
import com.medusa.gruul.payment.api.util.GlobalConstant;
import com.medusa.gruul.payment.conf.PayProperty;
import com.medusa.gruul.payment.mapper.PaymentRefundMapper;
import com.medusa.gruul.payment.service.EntPayService;
import com.medusa.gruul.payment.service.PaymentRefundService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author create by zq
 * @date created in 2019/11/18
 */
@Service(value = "paymentRefundServiceImpl")
@Log
public class PaymentRefundServiceImpl extends ServiceImpl<PaymentRefundMapper, PaymentRefund> implements PaymentRefundService {

    @Autowired
    private PayProperty payProperty;

    @Autowired
    private EntPayService entPayService;


    /**
     * 退款
     *
     * @param param
     * @throws WxPayException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result payRefund(RefundRequestDto param) {
        Result checkResult = new Result();
        if (param.checkParam(checkResult)) {
            return checkResult;
        }

        EntPayReQuestDto dto = new EntPayReQuestDto();
        dto.setTenantId(param.getTenantId());

        WxPayRefundRequest wxPayRefundRequest = new WxPayRefundRequest();
        wxPayRefundRequest.setTotalFee(param.getTotalFee());
        wxPayRefundRequest.setOutTradeNo(param.getOrderId());
        wxPayRefundRequest.setOutRefundNo(param.getOrderId());
        wxPayRefundRequest.setRefundFee(param.getTotalFee());
        wxPayRefundRequest.setNotifyUrl(payProperty.getWxRefundUrl());

        PaymentRefund refundModel = new PaymentRefund();
        refundModel.setOrderId(param.getOrderId());
        refundModel.setAsynRequest(JSONObject.toJSONString(param));
        refundModel.setTenantId(param.getTenantId());
        refundModel.setRouteKey(param.getRouteKey());
        this.save(refundModel);

        WxPayService wxPayService = entPayService.getWxPayService(dto);
        try {
            log.warning("退款参数 : " + JSONObject.toJSONString(wxPayRefundRequest));

            WxPayRefundResult refund = wxPayService.refund(wxPayRefundRequest);
            String result = JSONObject.toJSONString(refund);

            log.warning("退款同步返回 : " + result);
            refundModel.setAsynResult(result);
            this.updateById(refundModel);

            String resultCode = refund.getResultCode();

            log.warning("退款同步返回结果CODE : " + resultCode);
            if (GlobalConstant.STRING_SUCCESS.equals(resultCode)) {
                return Result.ok(resultCode);
            }
            return Result.failed(refund.getErrCodeDes());
        } catch (Exception e) {
            log.warning("退款失败 失败信息 : " + e.getMessage());
            return Result.failed(e.getMessage());
        }
    }

}
