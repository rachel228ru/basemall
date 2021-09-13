package com.medusa.gruul.payment.handler;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.order.WxPayMwebOrderResult;
import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceApacheHttpImpl;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.LocalDateTimeUtils;
import com.medusa.gruul.payment.api.constant.ReturnCodeConstant;
import com.medusa.gruul.payment.api.constant.StatusConstant;
import com.medusa.gruul.payment.api.entity.Payment;
import com.medusa.gruul.payment.api.entity.PaymentRecord;
import com.medusa.gruul.payment.api.entity.PaymentWechat;
import com.medusa.gruul.payment.api.enums.WxPayEnum;
import com.medusa.gruul.payment.api.model.dto.PayRequestDto;
import com.medusa.gruul.payment.api.model.dto.PayResultDto;
import com.medusa.gruul.payment.api.model.dto.WxPayResultDto;
import com.medusa.gruul.payment.conf.PayProperty;
import com.medusa.gruul.payment.service.IPaymentRecordService;
import com.medusa.gruul.payment.service.IPaymentService;
import com.medusa.gruul.payment.service.IPaymentWechatService;
import com.medusa.gruul.platform.api.entity.MiniInfo;
import com.medusa.gruul.platform.api.feign.RemoteMiniInfoService;
import com.medusa.gruul.platform.api.model.dto.ShopConfigDto;
import com.medusa.gruul.platform.api.model.vo.PayInfoVo;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author whh
 */
@Component
public class WxPayMiniHandler extends AbstractHandler {

    @Autowired
    private IPaymentService paymentService;

    @Autowired
    private IPaymentRecordService paymentRecordService;

    @Autowired
    private IPaymentWechatService paymentWechatService;

    @Autowired
    private PayProperty payProperty;

    @Autowired
    private RemoteMiniInfoService remoteMiniInfoService;

    /**
     * 超时关闭订单
     */
    private static ScheduledExecutorService EXECUTOR_SERVICE = new ScheduledThreadPoolExecutor(5,
            new BasicThreadFactory.Builder().namingPattern("transactionCloseTime-schedule-pool-%d").daemon(true).build());


    @Override
    @Transactional(rollbackFor = Exception.class)
    public PayResultDto handle(PayRequestDto payRequestDto) throws ServiceException {
        WxPayEnum wxPayType = WxPayEnum.getPayByTradeType(payRequestDto.getTradeType());
        switch (wxPayType) {
            case JSAPI_MINI: {
                return wxJsApiPay(payRequestDto);
            }
            case JSAPI_MP: {
                return wxJsApiPay(payRequestDto);
            }
            case H5: {
                return wxH5Pay(payRequestDto);
            }
            default: {
                throw new ServiceException("TradeType未匹配对应wxPay支付.");
            }
        }
    }


    /**
     * wx pay h5
     * @param payRequestDto
     * @return PayResultDto
     */
    public PayResultDto wxH5Pay(PayRequestDto payRequestDto) {
        PayResultDto test = this.test(payRequestDto);
        //校验指定模式下的数据是否为空
        if (test != null) {
            return test;
        }

        //记录调用信息
        Payment payment = this.generatePayment(payRequestDto, payProperty.getWorkerId(), payProperty.getDatacenterId());
        paymentService.save(payment);

        //记录渠道信息数据
        PaymentWechat paymentWechat = new PaymentWechat();
        paymentWechat.setTradeType(WxPayEnum.H5.getType());
        paymentWechat.setPaymentId(payment.getId());
        paymentWechat.setOutTradeNo(payRequestDto.getOutTradeNo());
        paymentWechat.setSubject(payRequestDto.getSubject());
        paymentWechat.setTenantId(payRequestDto.getTenantId());
        paymentWechat.setOpenId(payRequestDto.getOpenId());
        paymentWechatService.save(paymentWechat);

        //记录业务数据
        PaymentRecord paymentRecord = this.generateRecod(payRequestDto);
        paymentRecord.setPaymentId(payment.getId());


        //获取支付配置,并封装数据调用支付接口
        WxPayService wxPayService = getWxPayService(payment.getTenantId(), payment.getBusinessNotifyUrl(), payRequestDto.getTradeType());
        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
        orderRequest.setBody(payment.getBody());
        orderRequest.setOutTradeNo(payment.getTransactionId().toString());
        orderRequest.setTotalFee(BaseWxPayRequest.yuanToFen(payment.getTotalFee().toString()));
        orderRequest.setOpenid(payRequestDto.getOpenId());
        orderRequest.setSpbillCreateIp(payment.getTerminalIp());
        orderRequest.setNonceStr(IdUtil.simpleUUID());
        orderRequest.setTradeType(WxPayConstants.TradeType.MWEB);
        orderRequest.setNotifyUrl(payProperty.getNotify());
        orderRequest.setAttach(payment.getBusinessParams());
        orderRequest.setSignType("MD5");
        test = new PayResultDto();
        WxPayResultDto wxPayResultDto = new WxPayResultDto();
        WxPayMwebOrderResult wxPayMwebOrderResult = null;
        try {
            wxPayMwebOrderResult = wxPayService.createOrder(orderRequest);
            paymentRecord.setSendParam(JSON.toJSONString(orderRequest));
            paymentRecordService.save(paymentRecord);
            wxPayResultDto.setMWebUrl(wxPayMwebOrderResult.getMwebUrl());
            wxPayResultDto.setResultCode("SUCCESS");
            wxPayResultDto.setTradeType(orderRequest.getTradeType());
            wxPayResultDto.setTransactionId(payment.getTransactionId().toString());
            test.setWxResult(wxPayResultDto);
            transactionCloseTime(payment);
            return test;
        } catch (WxPayException e) {
            test.setReturnCode(e.getReturnCode());
            test.setReturnMsg(e.getReturnMsg());
            wxPayResultDto.setResultCode(e.getResultCode());
            wxPayResultDto.setErrCode(e.getErrCode());
            wxPayResultDto.setErrCodeDes(e.getErrCodeDes());
            test.setWxResult(wxPayResultDto);
            return test;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }


    /**
     * wx pay jsApi
     * @param payRequestDto
     * @return PayResultDto
     */
    public PayResultDto wxJsApiPay(PayRequestDto payRequestDto) {
        PayResultDto test = this.test(payRequestDto);
        //校验指定模式下的数据是否为空
        if (test != null) {
            return test;
        }

        //记录调用信息
        Payment payment = this.generatePayment(payRequestDto, payProperty.getWorkerId(), payProperty.getDatacenterId());
        paymentService.save(payment);

        //记录渠道信息数据
        PaymentWechat paymentWechat = new PaymentWechat();
        paymentWechat.setTradeType(payRequestDto.getTradeType());
        paymentWechat.setPaymentId(payment.getId());
        paymentWechat.setOutTradeNo(payRequestDto.getOutTradeNo());
        paymentWechat.setSubject(payRequestDto.getSubject());
        paymentWechat.setTenantId(payRequestDto.getTenantId());
        paymentWechat.setOpenId(payRequestDto.getOpenId());
        paymentWechatService.save(paymentWechat);

        //记录业务数据
        PaymentRecord paymentRecord = this.generateRecod(payRequestDto);
        paymentRecord.setPaymentId(payment.getId());


        //获取支付配置,并封装数据调用支付接口
        WxPayService wxPayService = getWxPayService(payment.getTenantId(), payment.getBusinessNotifyUrl(), payRequestDto.getTradeType());
        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
        orderRequest.setBody(payment.getBody());
        orderRequest.setOutTradeNo(payment.getTransactionId().toString());
        orderRequest.setTotalFee(BaseWxPayRequest.yuanToFen(payment.getTotalFee().toString()));
        orderRequest.setOpenid(payRequestDto.getOpenId());
        orderRequest.setSpbillCreateIp(payment.getTerminalIp());
        orderRequest.setNonceStr(IdUtil.simpleUUID());
        orderRequest.setTradeType(WxPayConstants.TradeType.JSAPI);
        orderRequest.setNotifyUrl(payProperty.getNotify());
        orderRequest.setAttach(payment.getBusinessParams());
        orderRequest.setSignType("MD5");
        test = new PayResultDto();
        WxPayResultDto wxPayResultDto = new WxPayResultDto();
        WxPayMpOrderResult wxPayUnifiedOrderResult = null;
        try {
            wxPayUnifiedOrderResult = wxPayService.createOrder(orderRequest);
            paymentRecord.setSendParam(JSON.toJSONString(orderRequest));
            paymentRecordService.save(paymentRecord);
            wxPayResultDto.setAppId(wxPayUnifiedOrderResult.getAppId());
            wxPayResultDto.setNonceStr(wxPayUnifiedOrderResult.getNonceStr());
            wxPayResultDto.setPackageValue(wxPayUnifiedOrderResult.getPackageValue());
            wxPayResultDto.setPaySign(wxPayUnifiedOrderResult.getPaySign());
            wxPayResultDto.setSignType(wxPayUnifiedOrderResult.getSignType());
            wxPayResultDto.setTimeStamp(wxPayUnifiedOrderResult.getTimeStamp());
            wxPayResultDto.setResultCode("SUCCESS");
            wxPayResultDto.setTradeType(orderRequest.getTradeType());
            wxPayResultDto.setTransactionId(payment.getTransactionId().toString());
            test.setWxResult(wxPayResultDto);
            transactionCloseTime(payment);
            return test;
        } catch (WxPayException e) {
            test.setReturnCode(e.getReturnCode());
            test.setReturnMsg(e.getReturnMsg());
            wxPayResultDto.setResultCode(e.getResultCode());
            wxPayResultDto.setErrCode(e.getErrCode());
            wxPayResultDto.setErrCodeDes(e.getErrCodeDes());
            test.setWxResult(wxPayResultDto);
            return test;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }


    /**
     * 超时关闭交易
     *
     * @param payment 记录
     */
    public void transactionCloseTime(Payment payment) {
        try {
            long delay = LocalDateTimeUtils.getMilliByTime(payment.getTimeoutExpress()) - System.currentTimeMillis();
            EXECUTOR_SERVICE.schedule(() -> {
                Payment p = paymentService.getById(payment.getId());
                if (p.getTradeStatus().equals(StatusConstant.TradeStatus.WAIT_BUYER_PAY)) {
                    p.setTradeStatus(StatusConstant.TradeStatus.TRADE_CLOSED);
                    paymentService.updateById(p);
                }
            }, delay, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
    }


    private WxPayService getWxPayService(String tenantId, String notify, Integer tradeType) {
        ShopConfigDto shopConfig = remoteMiniInfoService.getShopConfig(tenantId);
        if (shopConfig == null) {
            throw new ServiceException("商户配置不存在");
        }
        PayInfoVo payInfo = shopConfig.getPayInfo();
        if (payInfo == null) {
            throw new ServiceException("支付配置不存在");
        }

        WxPayService wxPayService = new WxPayServiceApacheHttpImpl();
        WxPayConfig wxPayConfig = new WxPayConfig();
        String appId = "";
        if (tradeType.equals(WxPayEnum.JSAPI_MP.getType())) {
            MiniInfo mpInfo = shopConfig.getMpInfo();
            if (mpInfo == null) {
                throw new ServiceException("不存在授权公众号");
            }
            if (mpInfo.getAuthorizerFlag().equals(CommonConstants.NUMBER_ZERO)) {
                throw new ServiceException("公众号未授权");
            }
            appId = mpInfo.getAppId();
        } else {
            MiniInfo miniInfo = shopConfig.getMiniInfo();
            if (miniInfo == null) {
                throw new ServiceException("未授权小程序");
            }
            appId = miniInfo.getAppId();
        }
        wxPayConfig.setAppId(appId);
        wxPayConfig.setMchId(payInfo.getMchId());
        wxPayConfig.setMchKey(payInfo.getMchKey());
        wxPayConfig.setKeyPath(payInfo.getCertificatesPath());
        wxPayConfig.setNotifyUrl(notify);

        wxPayConfig.setTradeType(WxPayConstants.TradeType.JSAPI);
        wxPayService.setConfig(wxPayConfig);
        return wxPayService;
    }

    @Override
    public PaymentRecord generateRecod(PayRequestDto payRequestDto) {
        return super.generateRecod(payRequestDto);
    }

    @Override
    public Payment generatePayment(PayRequestDto payRequestDto, long workerId, long datacenterId) {
        return super.generatePayment(payRequestDto, workerId, datacenterId);
    }

    @Override
    public PayResultDto test(PayRequestDto payRequestDto) {
        PayResultDto resultDto = new PayResultDto();
        resultDto.setReturnCode(ReturnCodeConstant.FAIL);
        //trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。
        if (StrUtil.isEmpty(payRequestDto.getOpenId())) {
            resultDto.setReturnMsg("JSAPI模式下,用户openId不能为空");
            return resultDto;
        }
        return null;
    }

}
