package com.medusa.gruul.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class AreaDto implements Serializable {

    private String label;
    private String value;
    private String fatherid;
    private List<AreaDto> list;
}
