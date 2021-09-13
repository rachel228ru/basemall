package com.medusa.gruul.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.platform.api.entity.AccountInfo;
import com.medusa.gruul.platform.api.entity.PlatformAccountBalanceRecord;

import java.math.BigDecimal;

/**
 * <p>
 * 账号余额明细表 服务类
 * </p>
 *
 * @author whh
 * @since 2020-08-01
 */
public interface IPlatformAccountBalanceRecordService extends IService<PlatformAccountBalanceRecord> {


    /**
     * 新增用户余额明细
     *
     * @param accountInfo     用户信息
     * @param consumptionType 变动类型 1:充值 2:套餐购买 3:套餐续费 4.代理充值
     * @param orderNumber     订单号
     * @param accountAmount   变动金额
     */
    void newBlanceDeail(AccountInfo accountInfo, Integer consumptionType, String orderNumber, BigDecimal accountAmount);
}
