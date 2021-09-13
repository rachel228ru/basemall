package com.medusa.gruul.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.medusa.gruul.platform.api.entity.SysShopPackageOrder;
import com.medusa.gruul.platform.model.dto.SysShopPackageOrderDto;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 店铺套餐订单表 Mapper 接口
 * </p>
 *
 * @author whh
 * @since 2020-08-01
 */
public interface SysShopPackageOrderMapper extends BaseMapper<SysShopPackageOrder> {

    /**
     * 查询所有用户订单
     *
     * @param objectPage   分页   0:待处理1:处理中2:已经完成 3:关闭
     * @param status       支付状态
     * @param payStartTime 开始时间
     * @param payEndTime   结束时间
     * @param phone        手机号
     * @param orderNum     充值订单号
     * @param nikeName     商户昵称
     * @param payType      支付方式 0-所有 1:余额支付2:微信3:支付宝4:汇款支付
     * @param templateId   模板id
     * @param userId       用户id
     * @return com.medusa.gruul.platform.model.dto.SysShopPackageOrderDto
     */
    IPage<SysShopPackageOrderDto> selectOrders(@Param("objectPage") Page<Object> objectPage, @Param("status") Integer status,
                                               @Param("payStartTime") String payStartTime, @Param("payEndTime") String payEndTime,
                                               @Param("phone") String phone, @Param("orderNum") String orderNum,
                                               @Param("nikeName") String nikeName, @Param("payType") Integer payType,
                                               @Param("templateId") Long templateId, @Param("userId") Long userId);


    /**
     * 获取历史套餐是否有大于门店版
     *
     * @param tenantId 租户id
     * @return 返回数量
     */
    Integer selectBoughtEnterpriseVersion(@Param("tenantId") String tenantId);

    /**
     * 获取代理有效的订单的实付金额
     *
     * @param agentId 代理id
     * @return 订单列表
     */
    List<BigDecimal> selectByAgentOrder(Long agentId);
}
