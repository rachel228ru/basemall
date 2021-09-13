package com.medusa.gruul.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.CurUserUtil;
import com.medusa.gruul.common.core.util.LocalDateTimeUtils;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.platform.api.entity.AccountInfo;
import com.medusa.gruul.platform.api.entity.PlatformAccountRecharge;
import com.medusa.gruul.platform.mapper.PlatformAccountRechargeMapper;
import com.medusa.gruul.platform.model.dto.BalanceRechargeDto;
import com.medusa.gruul.platform.model.dto.PayDto;
import com.medusa.gruul.platform.model.dto.PlatformAccountRechargeDto;
import com.medusa.gruul.platform.model.vo.*;
import com.medusa.gruul.platform.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 充值订单表 服务实现类
 * </p>
 *
 * @author whh
 * @since 2020-08-01
 */
@Service
@Slf4j
public class PlatformAccountRechargeServiceImpl extends ServiceImpl<PlatformAccountRechargeMapper, PlatformAccountRecharge> implements IPlatformAccountRechargeService {

    @Autowired
    private ISystemConfService systemConfService;

    @Autowired
    private IAccountInfoService accountInfoService;
    @Autowired
    private IPlatformAccountBalanceRecordService platformAccountBalanceRecordService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public BalanceRechargeVo balanceRecharge(BalanceRechargeDto balanceRechargeDto) {

        if (CommonConstants.NUMBER_THREE.equals(balanceRechargeDto.getPayType())) {
            if (StrUtil.isEmpty(balanceRechargeDto.getPayInfo())) {
                throw new ServiceException("付款方信息,请勿为空!");
            }
        }
        AccountInfo accountInfo = accountInfoService.getById(Long.valueOf(CurUserUtil.getPcRqeustAccountInfo().getUserId()));
        if (ObjectUtil.isNull(accountInfo)) {
            throw new ServiceException("用户数据错误");
        }
        SystemConfigVo systemConfigVo = systemConfService.getTypeInfo(CommonConstants.NUMBER_ZERO);
        if (ObjectUtil.isEmpty(systemConfigVo)) {
            throw new ServiceException("支付渠道错误,请联系平台!");
        }
        String notifyUrl = systemConfigVo.getSystemConfig().getMiniDomain().concat("/api/platform/recharge/balance/notify");
        PlatformAccountRecharge platformAccountRecharge = this.newPlatformAccountRecharge(accountInfo, balanceRechargeDto.getPayType(), balanceRechargeDto.getBalance(), balanceRechargeDto.getPayInfo(), "");

        try {
            BalanceRechargeVo vo = new BalanceRechargeVo();
            PlatformAccountRecharge up = new PlatformAccountRecharge();
            switch (balanceRechargeDto.getPayType()) {
                case 1:
                    PayDto payDto1 = systemConfService.aliPayQrcodePay(notifyUrl.concat("/alipay"), "余额充值", platformAccountRecharge.getPayAmount().toString(), platformAccountRecharge.getPayNum());
                    vo.setCodeUrl(payDto1.getCodeUrl());
                    break;
                case 2:
                    PayDto payDto = systemConfService.wxQrcodePay(notifyUrl.concat("/wx"), platformAccountRecharge.getPayNum(), platformAccountRecharge.getPayAmount().toString(), "用户余额充值", "blanceRecharge");
                    up.setPrepayId(payDto.getPrepayId());
                    vo.setCodeUrl(payDto.getCodeUrl());
                    break;
                case 3:
                    up.setPayInfo(balanceRechargeDto.getPayInfo());
                    up.setStatus(CommonConstants.NUMBER_ONE);
                    break;
                default:
                    throw new ServiceException("业务异常");
            }
            //更新数据
            this.baseMapper.update(up, new QueryWrapper<PlatformAccountRecharge>().eq("id", platformAccountRecharge.getId()));
            vo.setOrderId(platformAccountRecharge.getId());
            return vo;
        } catch (ServiceException serviceException) {
            serviceException.printStackTrace();
            throw new ServiceException(serviceException.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("支付异常,请联系平台!");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String rechargeNotifyWx(String xmlResult) {
        WxPayOrderNotifyResult payOrderNotifyResult = WxPayOrderNotifyResult.fromXML(xmlResult);
        String outTradeNo = payOrderNotifyResult.getOutTradeNo();
        if (payNotify(outTradeNo)) {
            return WxPayNotifyResponse.fail("处理成功");
        } else {
            return WxPayNotifyResponse.fail("处理失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String rechargeNotifyAlipay(Map<String, String> toMap) {
        if (MapUtil.isEmpty(toMap)) {
            log.warn("无效数据");
            return "fail";
        }
        String outTradeNo = toMap.get("out_trade_no");
        if (payNotify(outTradeNo)) {
            return "success";
        }
        return "fail";
    }

    /**
     * 指定订单回调处理
     *
     * @param outTradeNo 订单号
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean payNotify(String outTradeNo) {
        try {
            PlatformAccountRecharge recharge = this.selectByPayNum(outTradeNo);
            //不存在订单
            if (recharge == null) {
                return Boolean.FALSE;
            }
            //已处理
            if (CommonConstants.NUMBER_TWO.equals(recharge.getStatus())) {
                return Boolean.TRUE;
            }
            PlatformAccountRecharge up = new PlatformAccountRecharge();
            LocalDateTime date = LocalDateTimeUtils.convertDateToLDT(DateUtil.date());
            up.setFinishTime(date);
            up.setStatus(CommonConstants.NUMBER_TWO);
            if (recharge.getPayType().equals(CommonConstants.NUMBER_THREE)) {
                up.setAuditTime(date);
            }
            //更新订单状态
            boolean flag = this.update(up, new QueryWrapper<PlatformAccountRecharge>().eq("id", recharge.getId()));
            if (!flag) {
                log.info("订单未更新成功 orderId: {} ", outTradeNo);
                return Boolean.FALSE;
            }
            //修改用户余额
            AccountInfo accountInfo = accountInfoService.getById(recharge.getAccountId());
            AccountInfo upAccount = new AccountInfo();
            upAccount.setId(accountInfo.getId());
            upAccount.setBalance(NumberUtil.add(accountInfo.getBalance(), recharge.getPayAmount()));
            accountInfoService.updateById(upAccount);
            //增加用户余额明细
            platformAccountBalanceRecordService.newBlanceDeail(
                    accountInfo,
                    CommonConstants.NUMBER_ONE, recharge.getPayNum(), recharge.getPayAmount());

            if (accountInfo.getAgentId() != null && accountInfo.getAgentId() > 0) {
                String key2 = "";
                //1支付宝 2 微信 3汇款支付
                switch (recharge.getPayType()) {
                    case 1:
                        key2 = key2.concat("支付宝");
                        break;
                    case 2:
                        key2 = key2.concat("微信");
                        break;
                    case 3:
                        key2 = key2.concat("汇款支付");
                        break;
                    default:
                        break;
                }
                //充值通知
                MapBuilder<String, String> content = MapUtil.<String, String>builder()
                        .put("key1", recharge.getPayAmount().toString())
                        .put("key2", key2)
                        .put("key3", LocalDateTimeUtil.format(recharge.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                if (recharge.getPayType().equals(CommonConstants.NUMBER_THREE)) {
                    JSONObject jsonObject = JSONObject.parseObject(recharge.getPayInfo());
                    content.put("key4", jsonObject.getString("name"));
                    content.put("key5", jsonObject.getString("account"));
                    content.put("key6", DateUtil.parse(jsonObject.getString("paymentTime")).toDateStr());
                }
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Boolean.FALSE;
        }
    }

    /**
     * 查询指定订单号订单
     *
     * @param outTradeNo 订单号
     */
    private PlatformAccountRecharge selectByPayNum(String outTradeNo) {
        return this.getBaseMapper().selectOne(new QueryWrapper<PlatformAccountRecharge>().eq("pay_num", outTradeNo));
    }


    @Override
    public PageUtils<RechargeRecordOrderVo> payOrder(Integer page, Integer size, Integer status, String payStartTime, String payEndTime,
                                                     String phone, String rechargeNum, String nikeName,
                                                     Integer payType) {
        IPage<PlatformAccountRechargeDto> resultIpage = this.baseMapper.selectPayOrder(new Page<>(page, size),
                status, payStartTime, payEndTime, phone, rechargeNum, nikeName, payType);
        if (CollectionUtil.isEmpty(resultIpage.getRecords())) {
            return new PageUtils(null, (int) resultIpage.getTotal(),
                    (int) resultIpage.getSize(), (int) resultIpage.getCurrent());
        }
        List<RechargeRecordOrderVo> orderVos = resultIpage.getRecords().stream().map(obj -> BeanUtil.toBean(obj, RechargeRecordOrderVo.class)).collect(Collectors.toList());

        return new PageUtils<>(orderVos, (int) resultIpage.getTotal(),
                (int) resultIpage.getSize(), (int) resultIpage.getCurrent());

    }


    @Override
    public Boolean orderPayIfOk(Long orderId) {
        PlatformAccountRecharge platformAccountRecharge = this.getById(orderId);
        if (platformAccountRecharge == null) {
            throw new ServiceException("不存在该订单");
        }
        if (platformAccountRecharge.getStatus().equals(CommonConstants.NUMBER_TWO)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public PlatformAccountRecharge getByPrepayId(String prepayId) {
        return this.baseMapper.selectOne(new QueryWrapper<PlatformAccountRecharge>().eq("prepay_id", prepayId));
    }

    @Override
    public PlatformAccountRecharge newPlatformAccountRecharge(AccountInfo accountInfo, Integer payType, BigDecimal balance, String payInfo, String prepayId) {
        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        //设置代理订单
        PlatformAccountRecharge platformAccountRecharge = new PlatformAccountRecharge();
        platformAccountRecharge.setAccountId(accountInfo.getId());
        platformAccountRecharge.setRechargeNum(snowflake.nextIdStr());
        platformAccountRecharge.setPayNum(snowflake.nextIdStr());
        platformAccountRecharge.setPayType(payType);
        platformAccountRecharge.setPayAmount(balance);
        platformAccountRecharge.setAccountAmount(accountInfo.getBalance().add(balance));
        platformAccountRecharge.setStatus(CommonConstants.NUMBER_ZERO);
        platformAccountRecharge.setPaySource(CommonConstants.NUMBER_ONE);
        platformAccountRecharge.setInvoiceStatus(CommonConstants.NUMBER_ZERO);
        platformAccountRecharge.setPrepayId(prepayId);
        if (!this.save(platformAccountRecharge)) {
            throw new ServiceException("订单生成失败!!!");
        }
        return platformAccountRecharge;
    }

}
