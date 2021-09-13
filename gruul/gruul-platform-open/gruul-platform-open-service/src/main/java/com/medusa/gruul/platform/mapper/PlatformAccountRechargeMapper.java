package com.medusa.gruul.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.medusa.gruul.platform.api.entity.PlatformAccountRecharge;
import com.medusa.gruul.platform.model.dto.PlatformAccountRechargeDto;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 充值订单表 Mapper 接口
 * </p>
 *
 * @author whh
 * @since 2020-08-01
 */
public interface PlatformAccountRechargeMapper extends BaseMapper<PlatformAccountRecharge> {

    /**
     * 查询余额充值订单
     *
     * @param objectPage   分页查询
     * @param status       支付方式 0-所有 1-微信支付 2-支付宝支付  3-汇款支付
     * @param payStartTime 开始时间
     * @param payEndTime   结束时间
     * @param phone        手机号
     * @param rechargeNum  充值订单号
     * @param nikeName     商户昵称
     * @param payType      支付方式 0-所有 1-微信支付 2-支付宝支付  3-汇款支付
     * @return com.medusa.gruul.platform.api.entity.PlatformAccountRecharge
     */
    IPage<PlatformAccountRechargeDto> selectPayOrder(@Param("objectPage") Page<Object> objectPage, @Param("status") Integer status, @Param("payStartTime") String payStartTime,
                                                     @Param("payEndTime") String payEndTime, @Param("phone") String phone, @Param("rechargeNum") String rechargeNum, @Param("nikeName") String nikeName, @Param("payType") Integer payType);

}
