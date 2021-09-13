package com.medusa.gruul.account.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author whh
 * @description
 * @data: 2019/12/6
 */
@Data
public class UserBlacklistAstrictVo {

    @ApiModelProperty(value = "是否限制下单  1-未限制 2-已限制")
    private Boolean rejectOrder;

    @ApiModelProperty(value = "是否限制评论  1-未限制 2-已限制")
    private Boolean rejectComment;

}
