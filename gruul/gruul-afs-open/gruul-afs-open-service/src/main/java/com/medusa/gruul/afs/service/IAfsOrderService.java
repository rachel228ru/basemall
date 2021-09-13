package com.medusa.gruul.afs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.afs.api.entity.AfsOrder;
import com.medusa.gruul.afs.api.model.AfsSimpleVo;
import com.medusa.gruul.afs.model.*;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.common.dto.CurUserDto;
import com.medusa.gruul.order.api.entity.OrderSetting;
import com.medusa.gruul.order.api.model.OrderVo;

import java.util.List;

/**
 * <p>
 * 售后工单 服务类
 * </p>
 *
 * @author alan
 * @since 2020 -08-05
 */
public interface IAfsOrderService extends IService<AfsOrder> {

    /**
     * User apply afs order.
     *
     * @param dto the dto
     * @return the afs order
     */
    AfsOrder userApply(UserApplyDto dto);

    /**
     * Select progress by order id and product sku id afs order.
     *
     * @param orderId      the order id
     * @param productSkuId the product sku id
     * @return the afs order
     */
    AfsOrder selectProgressByOrderIdAndProductSkuId(Long orderId, Long productSkuId);

    /**
     * Gets user apply number.
     *
     * @param orderId the order id
     * @return the user apply number
     */
    Integer getUserApplyNumber(Long orderId);

    /**
     * User cancel.
     *
     * @param afsId    the afs id
     * @param isSystem the is system
     */
    void userCancel(Long afsId, boolean isSystem);

    /**
     * User return.
     *
     * @param afsId           the afs id
     * @param deliveryCode    the delivery code
     * @param deliveryCompany the delivery company
     * @param deliverySn      the delivery sn
     * @param phone           the phone
     * @param reason          the reason
     */
    void userReturn(Long afsId, String deliveryCode, String deliveryCompany, String deliverySn, String phone,
                    String reason);

    /**
     * Save afs order afs order.
     *
     * @param dto              the dto
     * @param curUserDto       the cur user dto
     * @param orderVo          the order vo
     * @param userAfterSaleNum the user after sale num
     * @param orderSetting     the order setting
     * @param receiptBillId    the receipt bill id
     * @return the afs order
     */
    AfsOrder saveAfsOrder(BaseApplyDto dto, CurUserDto curUserDto, OrderVo orderVo, Integer userAfterSaleNum,
                          OrderSetting orderSetting, Long receiptBillId);

    /**
     * Check order status.
     *
     * @param orderVo          the order vo
     * @param dto              the dto
     * @param orderSetting     the order setting
     * @param userAfterSaleNum the user after sale num
     */
    void checkOrderStatus(OrderVo orderVo, BaseApplyDto dto, OrderSetting orderSetting, Integer userAfterSaleNum);

    /**
     * Gets afs order info.
     *
     * @param afsId the afs id
     * @return the afs order info
     */
    AfsOrderVo getAfsOrderInfo(Long afsId);

    /**
     * Gets afs order by receipt bill id.
     *
     * @param receiptBillId the receipt bill id
     * @return the afs order by receipt bill id
     */
    List<AfsSimpleVo> getAfsOrderByReceiptBillId(Long receiptBillId);

    /**
     * Order receipt.
     *
     * @param orderVo the order vo
     */
    void orderReceipt(OrderVo orderVo);

    /**
     * Order shipped.
     *
     * @param orderVo the order vo
     */
    void orderShipped(OrderVo orderVo);

    /**
     * Search order page utils.
     *
     * @param dto the dto
     * @return the page utils
     */
    PageUtils<ApiAfsOrderVo> searchOrder(SearchOrderDto dto);

    /**
     * Order completed.
     *
     * @param orderVo the order vo
     */
    void orderCompleted(OrderVo orderVo);

    /**
     * Gets return address.
     *
     * @param orderId the order id
     * @return the return address
     */
    ReturnAddressVo getReturnAddress(Long orderId);

    /**
     * Gets afs expire.
     *
     * @param orderId the order id
     * @return the afs expire
     */
    Boolean getAfsExpire(Long orderId);

    /**
     * 发货单签收逻辑
     *
     * @param orderVo
     * @return void
     * @author alan
     * @date 2021/3/17 22:07
     */
    void deliverReceipt(OrderVo orderVo);
}
