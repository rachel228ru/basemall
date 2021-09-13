package com.medusa.gruul.afs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.afs.api.entity.AfsNegotiateHistory;
import com.medusa.gruul.afs.api.entity.AfsOrder;
import com.medusa.gruul.afs.api.entity.AfsOrderItem;
import com.medusa.gruul.afs.model.BaseApplyDto;
import com.medusa.gruul.afs.model.NegotiateHistoryDto;

import java.util.List;

/**
 * <p>
 * 协商历史 服务类
 * </p>
 *
 * @author alan
 * @since 2020 -08-05
 */
public interface IAfsNegotiateHistoryService extends IService<AfsNegotiateHistory> {

    /**
     * Init.
     *
     * @param afsOrderItemList the afs order item list
     * @param dto              the dto
     * @param name             the name
     */
    void init(List<AfsOrderItem> afsOrderItemList, BaseApplyDto dto, String name);

    /**
     * User cancel.
     *
     * @param afsOrder the afs order
     * @param isSystem the is system
     */
    void userCancel(AfsOrder afsOrder, boolean isSystem);

    /**
     * User return.
     *
     * @param afsOrder    the afs order
     * @param isLogistics the is logistics
     */
    void userReturn(AfsOrder afsOrder, boolean isLogistics);

    /**
     * Seller refuse.
     *
     * @param afsOrder the afs order
     */
    void sellerRefuse(AfsOrder afsOrder);


    /**
     * Seller shipped.
     *
     * @param afsOrder the afs order
     */
    void sellerShipped(AfsOrder afsOrder);

    /**
     * Seller approve need return.
     *
     * @param afsOrder    the afs order
     * @param isSystem    the is system
     * @param address     the address
     * @param isLogistics the is logistics
     */
    void sellerApproveNeedReturn(AfsOrder afsOrder, Boolean isSystem, String address, Boolean isLogistics);

    /**
     * Refund.
     *
     * @param afsOrder the afs order
     * @param isSystem the is system
     */
    void refund(AfsOrder afsOrder, boolean isSystem);

    /**
     * Negotiate history list list.
     *
     * @param dto the dto
     * @return the list
     */
    List<AfsNegotiateHistory> negotiateHistoryList(NegotiateHistoryDto dto);
}
