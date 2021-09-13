package com.medusa.gruul.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.order.api.entity.OrderShareSetting;

/**
 * <p>
 * 订单设置表 服务类
 * </p>
 *
 * @author alan
 * @since 2019 -09-02
 */
public interface IOrderShareSettingService extends IService<OrderShareSetting> {

    /**
     * Init.
     */
    void init();

    /**
     * Gets setting.
     *
     * @return the setting
     */
    OrderShareSetting getSetting();
}
