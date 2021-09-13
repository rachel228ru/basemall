package com.medusa.gruul.payment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.binarywang.wxpay.service.WxPayService;
import com.medusa.gruul.payment.api.entity.EntPay;
import com.medusa.gruul.payment.api.model.dto.EntPayReQuestDto;
import com.medusa.gruul.payment.api.model.param.EntPayReQuestParam;

/**
 * @author create by zq
 * @date created in 2019/11/18
 */
public interface EntPayService extends IService<EntPay> {


    /**
     * 请求 商家对个人付款
     *
     * @param dto
     * @return Result
     */
    EntPay pay(EntPayReQuestParam dto);


    /**
     * 组装配置
     *
     * @param dto
     * @return WxPayService
     */
    WxPayService getWxPayService(EntPayReQuestDto dto);

}
