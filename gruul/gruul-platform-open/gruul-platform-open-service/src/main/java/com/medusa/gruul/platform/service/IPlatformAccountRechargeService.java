package com.medusa.gruul.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.platform.api.entity.AccountInfo;
import com.medusa.gruul.platform.api.entity.PlatformAccountRecharge;
import com.medusa.gruul.platform.model.dto.BalanceRechargeDto;
import com.medusa.gruul.platform.model.vo.BalanceRechargeVo;
import com.medusa.gruul.platform.model.vo.RechargeRecordOrderVo;

import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 * 充值订单表 服务类
 * </p>
 *
 * @author whh
 * @since 2020-08-01
 */
public interface IPlatformAccountRechargeService extends IService<PlatformAccountRecharge> {

    /**
     * 用户充值接口
     *
     * @param balanceRechargeDto com.medusa.gruul.platform.model.dto.BalanceRechargeDto
     * @return com.medusa.gruul.platform.model.vo.BalanceRechargeVo
     */
    BalanceRechargeVo balanceRecharge(BalanceRechargeDto balanceRechargeDto);

    /**
     * 用户充值回调
     *
     * @param xmlResult 微信返回数据
     * @return success or fail
     */
    String rechargeNotifyWx(String xmlResult);


    /**
     * 支付宝回调
     *
     * @param toMap 回调数据
     * @return success or fail
     */
    String rechargeNotifyAlipay(Map<String, String> toMap);

    /**
     * 管理台分页查询充值订单
     *
     * @param page         指定页数
     * @param size         数据条数
     * @param status       0-所有 1-充值中 2-充值成功
     * @param payStartTime 开始时间
     * @param payEndTime   结束时间
     * @param phone        手机号
     * @param rechargeNum  充值订单号
     * @param nikeName     商户昵称
     * @param payType      支付方式 0-所有 1-微信支付 2-支付宝支付  3-汇款支付
     * @return com.medusa.gruul.platform.model.vo.RecordOrderVo
     */
    PageUtils<RechargeRecordOrderVo> payOrder(Integer page, Integer size, Integer status, String payStartTime, String payEndTime, String phone, String rechargeNum, String nikeName, Integer payType);



    /**
     * 查询指定订单是否支付
     *
     * @param orderId 订单id
     * @return java.lang.Boolean
     */
    Boolean orderPayIfOk(Long orderId);

    /**
     * 获取指定订单
     *
     * @param prepayId 订单id
     * @return com.medusa.gruul.platform.api.entity.PlatformAccountRecharge
     */
    PlatformAccountRecharge getByPrepayId(String prepayId);


    /**
     * 新增用户充值记录
     *
     * @param accountInfo 最新的用户信息
     * @param payType     支付方式
     * @param balance     支付余额
     * @param payInfo     付款方信息
     * @param prepayId    三方交易标识（代理代充为代理充值订单id）
     * @return com.medusa.gruul.platform.api.entity.PlatformAccountRecharge
     */
    PlatformAccountRecharge newPlatformAccountRecharge(AccountInfo accountInfo,  Integer payType, BigDecimal balance, String payInfo, String prepayId);
}
