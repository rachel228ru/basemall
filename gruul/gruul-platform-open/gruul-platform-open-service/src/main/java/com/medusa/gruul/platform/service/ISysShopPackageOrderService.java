package com.medusa.gruul.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.platform.api.entity.PlatformShopInfo;
import com.medusa.gruul.platform.api.entity.SysShopPackage;
import com.medusa.gruul.platform.api.entity.SysShopPackageOrder;
import com.medusa.gruul.platform.model.dto.OrderOptionDto;
import com.medusa.gruul.platform.model.dto.ShopPackageOrderDto;
import com.medusa.gruul.platform.model.vo.BuyPackageOrderVo;
import com.medusa.gruul.platform.model.vo.CalculateOrderPriceVo;
import com.medusa.gruul.platform.model.vo.PackageOrderConsoleVo;
import com.medusa.gruul.platform.model.vo.PackageOrderVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 店铺套餐订单表 服务类
 * </p>
 *
 * @author whh
 * @since 2020-08-01
 */
public interface ISysShopPackageOrderService extends IService<SysShopPackageOrder> {

    /**
     * 用户套餐操作(续费|购买|升级)
     *
     * @param shopPackageOrderDto com.medusa.gruul.platform.model.dto.ShopPackageOrderDto
     * @return com.medusa.gruul.platform.model.dto.PayDto
     */
    BuyPackageOrderVo optionPackage(ShopPackageOrderDto shopPackageOrderDto);

    /**
     * 套餐支付微信回调
     *
     * @param xmlResult 回调数据
     * @return success
     */
    String notifyWx(String xmlResult);

    /**
     * 管理台分页查询订购订单
     *
     * @param page         页数
     * @param size         数据条数
     * @param status       支付状态 0:待处理1:处理中2:已经完成 3:关闭
     * @param payStartTime 开始时间
     * @param payEndTime   结束时间
     * @param phone        手机号
     * @param orderNum     充值订单号
     * @param nikeName     商户昵称
     * @param payType      支付方式 0-所有 1:余额支付2:微信3:支付宝4:汇款支付     * @param templateId   模板id
     * @param userId
     * @return com.medusa.gruul.platform.model.vo.PackageOrderVo
     */
    PageUtils<PackageOrderVo> orders(Integer page, Integer size, Integer status, String payStartTime,
                                     String payEndTime, String phone, String orderNum, String nikeName, Integer payType, Long templateId, Long userId);


    /**
     * 操作订单
     *
     * @param dto com.medusa.gruul.platform.model.dto.PackageOrderOptionDto
     */
    void orderOption(OrderOptionDto dto);

    /**
     * 商户查询自己的套餐订单
     *
     * @param page         页数
     * @param size         条数
     * @param status       订单状态
     * @param payStartTime 订单创建开始时间
     * @param payEndTime   订单创建结束时间
     * @param packageId
     * @param platfromType
     * @param selectType
     * @return com.medusa.gruul.platform.model.vo.PackageOrderConsoleVo
     */
    PageUtils<PackageOrderConsoleVo> consoleOrders(Integer page, Integer size, Integer status, String payStartTime, String payEndTime, Long packageId, Integer platfromType, Integer selectType);

    /**
     * 查询指定订单是否支付成功
     *
     * @param orderId 订单id
     * @return java.lang.Boolean
     */
    Boolean orderPayIfOk(Long orderId);

    /**
     * 管理台(续费|购买|升级)
     *
     * @param shopPackageOrderDto com.medusa.gruul.platform.model.dto.ShopPackageOrderDto
     */
    void adminBuy(ShopPackageOrderDto shopPackageOrderDto);

    /**
     * 计算用户套餐操作(续费|购买|升级)价格
     *
     * @param shopPackageOrderDto com.medusa.gruul.platform.model.dto.ShopPackageOrderDto
     * @return 计算后的价格
     */
    CalculateOrderPriceVo calculateOrderPrice(ShopPackageOrderDto shopPackageOrderDto);

    /**
     * 管理台创建店铺购买
     *
     * @param orderSource     订单来源   0-用户购买 1-管理台购买 2-平台赠送 3-平台店铺创建
     * @param info            店铺信息
     * @param sysShopPackage  套餐信息
     * @param orderPirce      订单价格
     * @param givePackageTime 赠送时长
     */
    void adminCreateShopBuy(Integer orderSource, PlatformShopInfo info, SysShopPackage sysShopPackage, BigDecimal orderPirce, Integer givePackageTime);

    /**
     * 支付宝支付回调
     *
     * @param toMap 回调参数
     * @return sucess
     */
    String notifyAlipay(Map<String, String> toMap);


    /**
     * 获取历史套餐是否有大于门店版
     *
     * @param tenantId 租户id
     * @return 返回数量
     */
    Integer selectBoughtEnterpriseVersion(String tenantId);

    /**
     * 获取指定订单编号订单
     *
     * @param orderNum 订单标号
     * @return com.medusa.gruul.platform.api.entity.SysShopPackageOrder
     */
    SysShopPackageOrder getByOrderNum(String orderNum);

    /**
     * 获取代理有效的订单的实付金额
     *
     * @param agentId 代理id
     * @return java.math.BigDecimal
     */
    List<BigDecimal> selectByAgentOrder(Long agentId);
}
