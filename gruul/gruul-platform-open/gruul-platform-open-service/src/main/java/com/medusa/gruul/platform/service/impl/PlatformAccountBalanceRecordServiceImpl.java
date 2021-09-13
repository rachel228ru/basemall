package com.medusa.gruul.platform.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.platform.api.entity.AccountInfo;
import com.medusa.gruul.platform.api.entity.PlatformAccountBalanceRecord;
import com.medusa.gruul.platform.mapper.PlatformAccountBalanceRecordMapper;
import com.medusa.gruul.platform.service.IPlatformAccountBalanceRecordService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * <p>
 * 账号余额明细表 服务实现类
 * </p>
 *
 * @author whh
 * @since 2020-08-01
 */
@Service
public class PlatformAccountBalanceRecordServiceImpl extends ServiceImpl<PlatformAccountBalanceRecordMapper, PlatformAccountBalanceRecord> implements IPlatformAccountBalanceRecordService {


    @Override
    public void newBlanceDeail(AccountInfo accountInfo, Integer consumptionType, String orderNum, BigDecimal accountAmount) {
        PlatformAccountBalanceRecord platformAccountBalanceRecord = new PlatformAccountBalanceRecord();
        platformAccountBalanceRecord.setAccountId(accountInfo.getId());
        platformAccountBalanceRecord.setConsumptionType(consumptionType);
        platformAccountBalanceRecord.setOrderNum(orderNum);
        platformAccountBalanceRecord.setBeforeAmount(accountInfo.getBalance());
        platformAccountBalanceRecord.setAfterAmount(NumberUtil.add(accountInfo.getBalance(), accountAmount));
        platformAccountBalanceRecord.setAmount(accountAmount);
        //1:充值 2:套餐购买 3:套餐续费,4.代理充值
        if (CommonConstants.NUMBER_ONE.equals(consumptionType) || CommonConstants.NUMBER_FOUR.equals(consumptionType)) {
            platformAccountBalanceRecord.setType(CommonConstants.NUMBER_ONE);
        } else {
            platformAccountBalanceRecord.setType(CommonConstants.NUMBER_TWO);
        }
        this.baseMapper.insert(platformAccountBalanceRecord);
    }
}
