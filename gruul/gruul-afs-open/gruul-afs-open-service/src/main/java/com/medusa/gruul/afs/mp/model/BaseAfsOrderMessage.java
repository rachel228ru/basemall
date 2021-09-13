package com.medusa.gruul.afs.mp.model;

import lombok.Data;

/**
 * @author alan
 */
@Data
public class BaseAfsOrderMessage {
    private Long id;
    private String tenantId;
    private String shopId;
}
