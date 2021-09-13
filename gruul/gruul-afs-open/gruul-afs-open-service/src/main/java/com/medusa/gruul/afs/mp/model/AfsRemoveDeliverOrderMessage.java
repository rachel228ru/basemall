package com.medusa.gruul.afs.mp.model;

import lombok.Data;

/**
 * @author alan
 */
@Data
public class AfsRemoveDeliverOrderMessage {
    private Long orderId;
    private int type;
    private String tenantId;
    private String shopId;
}
