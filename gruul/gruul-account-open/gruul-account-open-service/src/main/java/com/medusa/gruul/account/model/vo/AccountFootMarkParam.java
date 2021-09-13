package com.medusa.gruul.account.model.vo;

import com.medusa.gruul.common.core.param.QueryParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author  xiaoq
 *
 * @data 2020/2/28 12:17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "AccountFootMarkParam对象", description = "分页查询参数")
public class AccountFootMarkParam extends QueryParam {


    private static final long serialVersionUID = 1L;

    private String userId;
}
