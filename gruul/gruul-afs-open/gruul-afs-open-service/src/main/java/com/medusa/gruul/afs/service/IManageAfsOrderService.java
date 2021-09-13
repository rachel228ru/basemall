package com.medusa.gruul.afs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.afs.api.entity.AfsOrder;
import com.medusa.gruul.afs.model.ManageAfsOrderVo;
import com.medusa.gruul.afs.model.SearchDto;
import com.medusa.gruul.afs.model.SellerNoteDto;
import com.medusa.gruul.afs.model.SellerRefusalDto;
import com.medusa.gruul.common.core.util.PageUtils;

/**
 * <p>
 * 售后工单 服务类
 * </p>
 *
 * @author alan
 * @since 2020 -08-05
 */
public interface IManageAfsOrderService extends IService<AfsOrder> {

    /**
     * Seller refuse.
     *
     * @param dto the dto
     */
    void sellerRefuse(SellerRefusalDto dto);

    /**
     * Seller approve.
     *
     * @param afsId    the afs id
     * @param isSystem the is system
     */
    void sellerApprove(Long afsId, Boolean isSystem);

    /**
     * Note.
     *
     * @param dto the dto
     */
    void note(SellerNoteDto dto);

    /**
     * User apply refund.
     *
     * @param afsOrder the afs order
     * @param isSystem the is system
     */
    void userApplyRefund(AfsOrder afsOrder, boolean isSystem);

    /**
     * User apply return.
     *
     * @param afsOrder   the afs order
     * @param isSystem   the is system
     * @param needReturn the need return
     */
    void userApplyReturn(AfsOrder afsOrder, Boolean isSystem, boolean needReturn);

    /**
     * Search manage afs order vo page page utils.
     *
     * @param dto the dto
     * @return the page utils
     */
    PageUtils<ManageAfsOrderVo> searchManageAfsOrderVoPage(SearchDto dto);
}
