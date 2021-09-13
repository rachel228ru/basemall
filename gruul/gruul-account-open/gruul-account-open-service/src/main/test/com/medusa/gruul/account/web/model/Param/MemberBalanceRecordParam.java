package com.medusa.gruul.account.web.model.Param;

import lombok.Data;

/**
 * @Description
 * @Author zhaokw
 * @Date 10:20 2020\8\23 0023
 **/
@Data
public class MemberBalanceRecordParam {
    private Integer page;
    private Integer size;
    private String yearMonth;
    private String memberInfoId;
    private String userShopId;
    private Integer dealType;
}
