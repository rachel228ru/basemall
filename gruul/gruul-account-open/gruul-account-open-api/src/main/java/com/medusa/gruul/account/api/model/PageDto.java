package com.medusa.gruul.account.api.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author whh
 * @description
 * @data: 2020/5/12
 */
@Data
public class PageDto<T> implements Serializable {

    private List<T> list;

    private Long size;
    private Long total;
    private Long pages;
    private Long current;
}
