package com.medusa.gruul.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.platform.api.entity.SysShopInvoiceOrder;
import com.medusa.gruul.platform.model.dto.InvoiceOrderAuditDto;
import com.medusa.gruul.platform.model.dto.ShopInvoiceOrderApplyDto;
import com.medusa.gruul.platform.model.vo.InvoiceOrderApplyVo;

import java.util.List;

/**
 * <p>
 * 发票工单表 服务类
 * </p>
 *
 * @author whh
 * @since 2020-08-01
 */
public interface ISysShopInvoiceOrderService extends IService<SysShopInvoiceOrder> {

    /**
     * 发票工单申请表
     *
     * @param orderApplyDto com.medusa.gruul.platform.model.dto.ShopInvoiceOrderApplyDto
     */
    void apply(ShopInvoiceOrderApplyDto orderApplyDto);

    /**
     * 查询发票工单
     *
     * @param page      页数
     * @param size      条数
     * @param type      抬头类型 1个人或事业单位，2企业
     * @param startTime 区间开始时间
     * @param endTime   区间结束时间
     * @param status    工单状态 0待审核，1已审核
     * @return com.medusa.gruul.platform.model.vo.InvoiceOrderApplyListVo
     */
    PageUtils<InvoiceOrderApplyVo> applyList(Integer page, Integer size, Integer type, String startTime, String endTime, Integer status);

    /**
     * 发票工单审核表
     *
     * @param orderAuditDto com.medusa.gruul.platform.model.dto.InvoiceOrderAuditDto
     */
    void audit(InvoiceOrderAuditDto orderAuditDto);

    /**
     * 查询指定类型订单,编号
     *
     * @param orderType 1-充值订单  2-套餐订购订单
     * @param orderIds  订单ids
     * @return com.medusa.gruul.platform.model.vo.InvoiceOrderApplyVo
     */
    List<InvoiceOrderApplyVo> getByOrderTypeAndOrderIds(Integer orderType, List<Long> orderIds);

}
