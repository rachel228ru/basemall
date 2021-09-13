package com.medusa.gruul.platform.model.dto;

import com.medusa.gruul.platform.api.entity.AuditRecord;
import lombok.Data;

/**
 * @author whh
 */
@Data
public class AuditRecordDto extends AuditRecord {

    /**
     * 审核版本
     */
    private String version;
}
