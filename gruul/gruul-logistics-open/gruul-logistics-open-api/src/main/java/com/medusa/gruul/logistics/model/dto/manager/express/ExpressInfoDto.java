package com.medusa.gruul.logistics.model.dto.manager.express;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * 快递接口数据传输对象
 * </p>
 *
 * @author lcysike
 * @since 2020-03-13
 */
@Data
public class ExpressInfoDto {

    /**
     * 订单编号，必须唯一
     */
    private String orderId;
    /**
     * 快递公司标识符，如：zt 。
     */
    private String shipperType;
    /**
     * 货品或者包裹名称
     */
    private String tradeName;
    /**
     * 收件人名称
     */
    private String name;
    /**
     * 收件人手机
     */
    private String mobile;
    /**
     * 收件人所在省
     */
    private String province;
    /**
     * 收件人所在市
     */
    private String city;
    /**
     * 收件人所在县/区
     */
    private String district;
    /**
     * 收件人详细地址
     */
    private String address;

}
