package com.medusa.gruul.payment.api.model.dto;

import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.util.Result;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author zq
 * @date 2019/11/06
 */
@Data
public class RefundRequestDto {

    /**
     * tenantId
     */
    @ApiModelProperty(value = "租户id")
    private String tenantId;

    /**
     * orderId
     */
    @ApiModelProperty(value = "订单id")
    private String orderId;

    /**
     * routeKey
     */
    @ApiModelProperty(value = "路由键,路由键和回调url必须选一个")
    private String routeKey;

    /**
     * totalFee
     */
    @ApiModelProperty(value = "退款总金额")
    private Integer totalFee;


    public boolean checkParam(Result checkResult) {
        if (StringUtils.isBlank(orderId)) {
            checkResult.setMsg("订单号不能为空");
            checkResult.setCode(CommonConstants.FAIL);
            return Boolean.TRUE;
        }

        if (StringUtils.isBlank(tenantId)) {
            checkResult.setMsg("租户id不能为空");
            checkResult.setCode(CommonConstants.FAIL);
            return Boolean.TRUE;
        }

        if (StringUtils.isBlank(routeKey)) {
            checkResult.setMsg("路由键不能为空");
            checkResult.setCode(CommonConstants.FAIL);
            return Boolean.TRUE;
        }

        if (null == totalFee || totalFee < 1) {
            checkResult.setMsg("金额不能为空");
            checkResult.setCode(CommonConstants.FAIL);
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }
}
