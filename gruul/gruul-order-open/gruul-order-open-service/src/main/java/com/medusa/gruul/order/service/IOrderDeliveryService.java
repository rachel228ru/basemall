package com.medusa.gruul.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.order.api.entity.OrderDelivery;
import com.medusa.gruul.order.api.model.GenerateSendBillOrderMessage;
import com.medusa.gruul.order.api.model.ReceiptSendBillOrderMessage;
import com.medusa.gruul.order.model.ReceiverAddressDto;

/**
 * <p>
 * 订单物流信息 服务类
 * </p>
 *
 * @author alan
 * @since 2019 -09-02
 */
public interface IOrderDeliveryService extends IService<OrderDelivery> {

    /**
     * 更新物流信息
     *
     * @param message the message
     * @return void
     * @author alan
     * @date 2019 /11/27 21:15
     */
    void updateShipping(GenerateSendBillOrderMessage message);


    /**
     * 签收发货单
     *
     * @param message the message
     * @return void
     * @author alan
     * @date 2019 /12/9 20:10
     */
    void receiptSendBill(ReceiptSendBillOrderMessage message);


    /**
     * 修改收货人地址
     *
     * @param dto the dto
     * @return void
     * @author alan
     * @date 2020 /7/3 20:30
     */
    void updateReceiverAddress(ReceiverAddressDto dto);
}
