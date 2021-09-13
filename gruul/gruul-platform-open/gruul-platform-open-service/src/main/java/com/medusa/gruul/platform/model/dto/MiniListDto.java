package com.medusa.gruul.platform.model.dto;


import com.medusa.gruul.platform.api.entity.MiniInfo;
import lombok.Data;

/**
 * @author whh
 */
@Data
public class MiniListDto extends MiniInfo {

    private String comboName;
    private String phone;
    private Integer auditStatus;
    private String reason;
    private String userVersion;
}
